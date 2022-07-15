import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { FileData, FileDataActionsState, Fileset, FileStatus } from '@backbase/ac-common-ang';
import { MimeTypes } from '@backbase/cm-ui-components-lib';

@Component({
  selector: 'bb-fileset-view',
  templateUrl: 'fileset-view.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilesetViewComponent {
  @Input() fileset!: Fileset;
  @Input() fileDataActions: { [key: string]: FileDataActionsState } = {};
  @Input() readOnly = false;
  @Output() download = new EventEmitter<FileData>();
  @Output() delete = new EventEmitter<string>();
  @Output() restore = new EventEmitter<string>();
  readonly fileStatus = FileStatus;

  downloadFileData(fileData: FileData): void {
    this.download.emit(fileData);
  }

  deleteFileData(fileId: string): void {
    this.delete.emit(fileId);
  }

  restoreFileData(fileId: string): void {
    this.restore.emit(fileId);
  }

  hasFiles(): boolean {
    return this.fileset.files && this.fileset.files.length > 0;
  }

  isDeleting(fileId: string): boolean {
    return Boolean(this.fileDataActions[fileId] && this.fileDataActions[fileId].delete.processing);
  }

  isDownloading(fileId: string): boolean {
    return Boolean(this.fileDataActions[fileId] && this.fileDataActions[fileId].download.processing);
  }

  getFileTypeIconName(mimeType: string): string {
    switch (mimeType) {
      case MimeTypes.BMP:
      case MimeTypes.WEBP:
      case MimeTypes.PNG:
      case MimeTypes.JPEG:
        return 'file-img';
      case MimeTypes.PDF:
        return 'file-pdf';
      case MimeTypes.CSV:
        return 'file-csv';
      case MimeTypes.XLS:
      case MimeTypes.XLSX:
        return 'file-xls';
      default:
        return 'file-unknown';
    }
  }
}
