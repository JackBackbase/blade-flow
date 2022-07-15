import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { FilesetViewComponent } from './fileset-view.component';

describe('FilesetViewComponent', () => {
  let component: FilesetViewComponent;
  let fixture: ComponentFixture<FilesetViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilesetViewComponent],
      schemas: [NO_ERRORS_SCHEMA],
    });

    fixture = TestBed.createComponent(FilesetViewComponent);
    component = fixture.componentInstance;
  });

  it('should create an instance of the component', () => {
    expect(component).toBeTruthy();
  });

  it('isDeleting should return false if there is no deleting process active', () => {
    const result = component.isDeleting('test');

    expect(result).toBeFalsy();
  });

  it('isDeleting should return true if there is deleting process active', () => {
    component.fileDataActions = {
      test: {
        download: {
          processing: false,
          httpError: undefined,
          success: true,
        },
        delete: {
          processing: true,
          httpError: undefined,
          success: true,
        },
        upload: {
          processing: false,
          httpError: undefined,
          success: true,
        },
      },
    };
    const result = component.isDeleting('test');

    expect(result).toBeTruthy();
  });

  it('isDownloading should return false if there is no deleting process active', () => {
    const result = component.isDownloading('test');

    expect(result).toBeFalsy();
  });

  it('isDownloading should return true if there is deleting process active', () => {
    component.fileDataActions = {
      test: {
        download: {
          processing: true,
          httpError: undefined,
          success: true,
        },
        delete: {
          processing: false,
          httpError: undefined,
          success: true,
        },
        upload: {
          processing: false,
          httpError: undefined,
          success: true,
        },
      },
    };
    const result = component.isDownloading('test');

    expect(result).toBeTruthy();
  });

  describe('getFileTypeIconName', () => {
    it('image/bmp should return proper type file', () => {
      const result = component.getFileTypeIconName('image/bmp');

      expect(result).toBe('file-img');
    });

    it('application/pdf should return proper type file', () => {
      const result = component.getFileTypeIconName('application/pdf');

      expect(result).toBe('file-pdf');
    });

    it('text/csv should return proper type file', () => {
      const result = component.getFileTypeIconName('text/csv');

      expect(result).toBe('file-csv');
    });

    it('application/vnd.ms-excel should return proper type file', () => {
      const result = component.getFileTypeIconName('application/vnd.ms-excel');

      expect(result).toBe('file-xls');
    });

    it('should return unknown type as default', () => {
      const result = component.getFileTypeIconName('test');

      expect(result).toBe('file-unknown');
    });
  });

  describe('#eventEmitters', () => {
    it('should emit #download', () => {
      const downloadEmitSpy = jest.spyOn(component.download, 'emit');
      component.downloadFileData({ id: 'someId' } as any);
      expect(downloadEmitSpy).toHaveBeenCalledWith({
        id: 'someId',
      } as any);
    });

    it('should emit #delete', () => {
      const deleteEmitSpy = jest.spyOn(component.delete, 'emit');
      component.deleteFileData('someFileRef');
      expect(deleteEmitSpy).toHaveBeenCalledWith('someFileRef');
    });

    it('should emit #restore', () => {
      const restoreEmiySpy = jest.spyOn(component.restore, 'emit');
      component.restoreFileData('someFileRef');
      expect(restoreEmiySpy).toHaveBeenCalledWith('someFileRef');
    });
  });

  describe('#hasFiles', () => {
    it('should return true when filesetDetails.files is defined', () => {
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
      expect(component.hasFiles()).toBeTruthy();
    });

    it('should return false when filesetDetails.files is not defined', () => {
      component.fileset = {
        id: 'someId',
        name: 'someName',
        createdBy: 'someName',
        createdAt: new Date().toISOString(),
        lastModifiedAt: new Date().toISOString(),
        files: [],
        allowedMediaTypes: [],
        maxFiles: 1,
      };
      expect(component.hasFiles()).toBeFalsy();
    });
  });

  describe('DOM data state', () => {
    const dataStateIdentifier = '*[data-role="fileset-view-data-state"]';

    it('should show the data state when fileset details has files', () => {
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
      const loadedStateElem: DebugElement = fixture.debugElement.query(By.css(dataStateIdentifier));

      expect(loadedStateElem).toBeTruthy();
    });
  });

  describe('DOM empty state', () => {
    const emptyStateIdentifier = '*[data-role="fileset-view-empty-state"]';

    it('should show the empty state when fileset details does not contain files', () => {
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
      const emptyStateElem: DebugElement = fixture.debugElement.query(By.css(emptyStateIdentifier));

      expect(emptyStateElem).toBeTruthy();
    });
  });
});
