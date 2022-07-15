import { NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {
  AcActionService,
  DocumentRequest,
  DocumentRequestListActionsState,
  DocumentRequestStatus,
} from '@backbase/ac-common-ang';
import { DataLoaderDirective } from '@backbase/ds-ui-components-ang';
import { LoadingIndicatorModule } from '@backbase/ui-ang';
import { MockDirective, MockModule } from 'ng-mocks';
import { BehaviorSubject } from 'rxjs';
import { take } from 'rxjs/operators';
import {
  AcDocumentRequestViewerWidgetComponent,
  DocumentRequestOverviewGroup,
} from './ac-document-request-viewer-widget.component';

const documentRequestListActions$$ = new BehaviorSubject<DocumentRequestListActionsState>({
  loading: { processing: false, httpError: undefined, success: false },
});
const documentRequestList$$ = new BehaviorSubject<DocumentRequest[]>([]);

class AcActionServiceStub implements Partial<AcActionService> {
  readonly documentRequestListActions$ = documentRequestListActions$$;
  readonly documentRequestList$ = documentRequestList$$;

  loadDocumentRequestList = jest.fn();
}

describe('AcDocumentRequestViewerWidgetComponent', () => {
  let component: AcDocumentRequestViewerWidgetComponent;
  let fixture: ComponentFixture<AcDocumentRequestViewerWidgetComponent>;
  let acActionService: AcActionService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [MockModule(LoadingIndicatorModule)],
      declarations: [MockDirective(DataLoaderDirective), AcDocumentRequestViewerWidgetComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: AcActionService,
          useClass: AcActionServiceStub,
        },
      ],
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcDocumentRequestViewerWidgetComponent);
    component = fixture.componentInstance;
    acActionService = TestBed.inject(AcActionService);
  });

  describe('when the component is being initialised', () => {
    it('should start loading document request list', () => {
      fixture.detectChanges();

      expect(acActionService.loadDocumentRequestList).toHaveBeenCalled();
    });
  });

  describe('when document request list id loaded', () => {
    it('should map to a document request reference group with different referenceIds', async () => {
      const expected = {
        'MO-1': {
          [DocumentRequestOverviewGroup.Open]: [
            {
              referenceId: 'MO-1',
              status: DocumentRequestStatus.Open,
            } as DocumentRequest,
          ],
        },
        'MO-2': {
          [DocumentRequestOverviewGroup.Open]: [
            {
              referenceId: 'MO-2',
              status: DocumentRequestStatus.Pending,
            } as DocumentRequest,
          ],
        },
      };
      documentRequestList$$.next([
        { referenceId: 'MO-1', status: DocumentRequestStatus.Open } as DocumentRequest,
        { referenceId: 'MO-2', status: DocumentRequestStatus.Pending } as DocumentRequest,
      ]);

      const actual = await component.requests$.pipe(take(1)).toPromise();

      expect(actual).toEqual(expected);
    });

    it('should map to a document request reference group with the same referenceId', async () => {
      const expected = {
        'MO-1': {
          [DocumentRequestOverviewGroup.Open]: [
            {
              referenceId: 'MO-1',
              status: DocumentRequestStatus.Open,
            } as DocumentRequest,
            {
              referenceId: 'MO-1',
              status: DocumentRequestStatus.Pending,
            } as DocumentRequest,
          ],
        },
      };
      documentRequestList$$.next([
        { referenceId: 'MO-1', status: DocumentRequestStatus.Open } as DocumentRequest,
        { referenceId: 'MO-1', status: DocumentRequestStatus.Pending } as DocumentRequest,
      ]);

      const actual = await component.requests$.pipe(take(1)).toPromise();

      expect(actual).toEqual(expected);
    });

    it('should map to a document request reference group with different overview groups', async () => {
      const expected = {
        'MO-1': {
          [DocumentRequestOverviewGroup.Open]: [
            {
              referenceId: 'MO-1',
              status: DocumentRequestStatus.Open,
            } as DocumentRequest,
          ],
          [DocumentRequestOverviewGroup.Done]: [
            {
              referenceId: 'MO-1',
              status: DocumentRequestStatus.Cancelled,
            } as DocumentRequest,
          ],
        },
      };
      documentRequestList$$.next([
        { referenceId: 'MO-1', status: DocumentRequestStatus.Open } as DocumentRequest,
        { referenceId: 'MO-1', status: DocumentRequestStatus.Cancelled } as DocumentRequest,
      ]);

      const actual = await component.requests$.pipe(take(1)).toPromise();

      expect(actual).toEqual(expected);
    });

    it('should count Rejected and Open tasks as active task', async () => {
      let actual: number;
      documentRequestList$$.next([
        { referenceId: 'MO-1', status: DocumentRequestStatus.Open } as DocumentRequest,
        { referenceId: 'MO-1', status: DocumentRequestStatus.Rejected } as DocumentRequest,
        { referenceId: 'MO-1', status: DocumentRequestStatus.Pending } as DocumentRequest,
      ]);

      actual = await component.activeTasksCounter$.pipe(take(1)).toPromise();

      expect(actual).toEqual(2);

      documentRequestList$$.next([
        { referenceId: 'MO-1', status: DocumentRequestStatus.Open } as DocumentRequest,
        { referenceId: 'MO-1', status: DocumentRequestStatus.Pending } as DocumentRequest,
      ]);

      actual = await component.activeTasksCounter$.pipe(take(1)).toPromise();

      expect(actual).toEqual(1);
    });
  });
});
