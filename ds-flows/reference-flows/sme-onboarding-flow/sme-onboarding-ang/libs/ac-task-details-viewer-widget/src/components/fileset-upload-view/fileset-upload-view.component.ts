import { Component, ChangeDetectionStrategy, Input, Output, EventEmitter } from '@angular/core';
import { of } from 'rxjs';
import { MIME_TYPE_MAP } from '@backbase/cm-ui-components-lib';
import { Fileset } from '@backbase/ac-common-ang';
import { FileUploadHandler } from '../fileset-container/fileset-container.component';

@Component({
  selector: 'bb-fileset-upload-view',
  templateUrl: 'fileset-upload-view.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class FilesetUploadViewComponent {
  @Input() fileset!: Fileset;
  @Input() multiple = true;
  @Output() upload = new EventEmitter<{ files: File[] }>();
  @Input() uploadHandler: FileUploadHandler = () => of({});

  get allowedMediaTypes(): string {
    return this.fileset.allowedMediaTypes.join(',');
  }

  get allowedMediaTypesFormatted(): string {
    const mediaTypes = this.fileset.allowedMediaTypes.map((type) => MIME_TYPE_MAP[type]).filter((x) => !!x);

    return mediaTypes.slice(1).reduce((acc, curr, idx) => {
      const isLast = mediaTypes.length - 2 - idx === 0;

      return `${acc}${!isLast ? ',' : ' and'} ${curr}`;
    }, mediaTypes[0]);
  }

  get maxFiles(): number {
    return this.fileset.maxFiles;
  }

  get maxFileSize(): number | undefined {
    return this.fileset.maxFileSize;
  }

  get filesLimit(): number {
    return this.maxFiles - this.fileset.files.length;
  }

  hasMaxFilesLimit(): boolean {
    return this.fileset.files.length >= this.fileset.maxFiles;
  }
}
