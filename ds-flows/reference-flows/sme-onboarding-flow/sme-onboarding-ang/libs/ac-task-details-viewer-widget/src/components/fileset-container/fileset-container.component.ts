import { Component, ChangeDetectionStrategy, Input, Output, EventEmitter } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { FileUploadEvent } from '@backbase/cm-ui-components-lib';
import {
  AcActionService,
  DocumentRequest,
  DocumentRequestStatus,
  FileData,
  Fileset,
  FileDataActionsState,
  downloadFile,
} from '@backbase/ac-common-ang';

export type FileUploadHandler = (file: File) => Observable<{}>;

@Component({
  selector: 'bb-fileset-container',
  templateUrl: 'fileset-container.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilesetContainerComponent {
  @Input() documentRequest!: DocumentRequest;
  @Output() upload = new EventEmitter<FileUploadEvent>();
  readonly fileset$: Observable<Fileset | undefined> = this.acActionService.fileset$;
  readonly requestLoading$: Observable<boolean> = this.acActionService.documentRequestActions$.pipe(
    map((actions) => actions.loading.processing),
  );
  readonly requestHttpError$: Observable<HttpErrorResponse | undefined> =
    this.acActionService.documentRequestActions$.pipe(map((actions) => actions.loading.httpError));
  readonly fileDataActions$: Observable<{
    [key: string]: FileDataActionsState;
  }> = this.acActionService.fileDataActions$;
  uploadHandler: FileUploadHandler = (file: File) => {
    return this.acActionService.uploadFiles([file], this.documentRequest.internalId, this.documentRequest.internalId);
  };

  constructor(private readonly acActionService: AcActionService) {}

  get readOnly(): boolean {
    return !(
      this.documentRequest?.status === DocumentRequestStatus.Open ||
      this.documentRequest?.status === DocumentRequestStatus.Rejected
    );
  }

  deleteFile(fileId: string): void {
    this.acActionService.deleteFile(fileId, this.documentRequest.internalId, this.documentRequest.internalId);
  }

  restoreFile(fileId: string): void {
    this.acActionService.deleteFile(fileId, this.documentRequest.internalId, this.documentRequest.internalId);
  }

  async downloadFile(file: FileData) {
    const tempGroupId =
      this.documentRequest.status === DocumentRequestStatus.Open ? this.documentRequest.internalId : undefined;
    const result = await this.acActionService.downloadFile(file.id, this.documentRequest.internalId, tempGroupId);

    downloadFile(result, file.name);
  }

  onUpload(event: FileUploadEvent): void {
    this.upload.emit(event);
  }
}
