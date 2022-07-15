import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { FileSizePipe } from '@backbase/ds-ui-components-ang';
import { FilesetUploadViewComponent } from './fileset-upload-view.component';

describe('FilesetUploadViewComponent', () => {
  let component: FilesetUploadViewComponent;
  let fixture: ComponentFixture<FilesetUploadViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilesetUploadViewComponent, FileSizePipe],
      schemas: [NO_ERRORS_SCHEMA],
    });

    fixture = TestBed.createComponent(FilesetUploadViewComponent);
    component = fixture.componentInstance;
  });

  it('should create an instance of the component', () => {
    expect(component).toBeTruthy();
  });

  it('should show formatted allowed media types', () => {
    component.fileset = {
      allowedMediaTypes: ['application/pdf', 'image/png', 'image/jpeg'],
    } as any;

    expect(component.allowedMediaTypesFormatted).toBe('PDF, PNG and JPEG');
  });

  describe('DOM has max files limit state', () => {
    const maxFilesLimitStateIdentifier = '*[data-role="fileset-upload-view-max-files-reached-text"]';

    it('should show the max files limit state when the max files limit is reached', () => {
      component.fileset = {
        id: 'someId',
        name: 'someName',
        createdBy: 'someName',
        createdAt: new Date().toISOString(),
        lastModifiedAt: new Date().toISOString(),
        files: [
          new File(['foo'], 'foo.txt', {
            type: 'text/plain',
          }) as any,
        ],
        allowedMediaTypes: [],
        maxFiles: 1,
      };
      fixture.detectChanges();
      const maxFilesLimitStateElem: DebugElement = fixture.debugElement.query(By.css(maxFilesLimitStateIdentifier));

      expect(maxFilesLimitStateElem).toBeTruthy();
    });
  });

  describe('DOM file upload state', () => {
    const fileUploadStateIdentifier = '*[data-role="fileset-upload-view-file-upload-component"]';

    it('should show the empty state when case fileset details does not contain files', () => {
      component.fileset = {
        id: 'someId',
        name: 'someName',
        createdBy: 'someName',
        createdAt: new Date().toISOString(),
        lastModifiedAt: new Date().toISOString(),
        files: [],
        allowedMediaTypes: [],
        maxFiles: 3,
      };
      fixture.detectChanges();
      const fileUploadStateElem: DebugElement = fixture.debugElement.query(By.css(fileUploadStateIdentifier));

      expect(fileUploadStateElem).toBeTruthy();
    });
  });
});
