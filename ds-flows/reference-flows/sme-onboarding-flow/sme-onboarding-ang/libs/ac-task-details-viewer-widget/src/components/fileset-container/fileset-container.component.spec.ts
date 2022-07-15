import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import {
  AcActionService,
  DocumentRequest,
  DocumentRequestActionsState,
  DocumentRequestStatus,
  Fileset,
} from '@backbase/ac-common-ang';
import { FileUploadEvent } from '@backbase/ds-ui-components-ang';
import { MockProvider } from 'ng-mocks';
import { BehaviorSubject, of } from 'rxjs';
import { FilesetContainerComponent } from './fileset-container.component';

const fileset$$ = new BehaviorSubject<Fileset>({
  id: 'someId',
  name: 'someName',
  createdBy: 'someName',
  createdAt: new Date().toISOString(),
  lastModifiedAt: new Date().toISOString(),
  files: [],
  allowedMediaTypes: [],
  maxFiles: 3,
});
const documentRequestActions$$ = new BehaviorSubject<DocumentRequestActionsState>({
  loading: { processing: false, httpError: undefined, success: false },
});

describe('FilesetContainerComponent', () => {
  let component: FilesetContainerComponent;
  let fixture: ComponentFixture<FilesetContainerComponent>;
  let acActionService: Partial<AcActionService>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilesetContainerComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        MockProvider(AcActionService, {
          documentRequestActions$: of({
            loading: { processing: false, httpError: undefined, success: false },
          }),
          deleteFile: jest.fn(),
        }),
      ],
    });

    fixture = TestBed.createComponent(FilesetContainerComponent);
    acActionService = TestBed.inject(AcActionService);
    component = fixture.componentInstance;
    component.documentRequest = {
      internalId: 'someId',
      status: DocumentRequestStatus.Open,
    } as DocumentRequest;
  });

  describe('#onDelete', () => {
    it('should delete a file when confirmed', () => {
      const deletionPendingFile = 'someFilename';
      fixture.detectChanges();
      component.deleteFile(deletionPendingFile);

      expect(acActionService.deleteFile).toHaveBeenCalled();
    });
  });

  describe('#restoreFile', () => {
    it('should restore file a file when confirmed', () => {
      const deletionPendingFile = 'someFilename';
      fixture.detectChanges();
      component.restoreFile(deletionPendingFile);

      expect(acActionService.deleteFile).toHaveBeenCalled();
    });
  });
  describe('DOM loaded state', () => {
    const loadedStateIdentifier = 'bb-fileset-view[data-role="fileset-details-loaded-state"]';

    it('should show the loaded state when the filesets loading request has succeded', () => {
      fileset$$.next({
        id: 'someId',
        name: 'someName',
        createdBy: 'someName',
        createdAt: new Date().toISOString(),
        lastModifiedAt: new Date().toISOString(),
        files: [],
        allowedMediaTypes: [],
        maxFiles: 3,
      });
      documentRequestActions$$.next({
        loading: {
          processing: false,
          httpError: undefined,
          success: true,
        },
      });
      fixture.detectChanges();
      const loadedStateElem: DebugElement = fixture.debugElement.query(By.css(loadedStateIdentifier));

      expect(loadedStateElem).toBeTruthy();
    });
  });

  it('onUpload should emit event', () => {
    jest.spyOn(component.upload, 'emit');

    const event: FileUploadEvent = {
      files: [],
      uploadedFilesCount: 1,
    };

    component.onUpload(event);

    expect(component.upload.emit).toHaveBeenCalledWith(event);
  });
});
