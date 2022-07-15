import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { merge, Observable } from 'rxjs';
import { map, filter, pluck } from 'rxjs/operators';
import { AcActionService, DocumentRequest, DocumentRequestStatus, Customer } from '@backbase/ac-common-ang';

export enum DocumentRequestOverviewGroup {
  Open = 'OPEN',
  Done = 'DONE',
}

type DocumentRequestGroup = Partial<Record<DocumentRequestOverviewGroup, DocumentRequest[]>>;

export interface DocumentRequestReferenceGroup {
  [key: string]: DocumentRequestGroup;
}

const isOpenRequest = (request: DocumentRequest) =>
  [DocumentRequestStatus.Open, DocumentRequestStatus.Rejected, DocumentRequestStatus.Pending].includes(request.status);

const makeDrReferenceGroup = (requests: DocumentRequest[]) =>
  requests.reduce((acc, request) => {
    const { referenceId } = request;
    const overviewStatus = isOpenRequest(request)
      ? DocumentRequestOverviewGroup.Open
      : DocumentRequestOverviewGroup.Done;

    if (acc[referenceId]) {
      if (acc[referenceId][overviewStatus]) {
        acc[referenceId][overviewStatus]?.push(request);
      } else {
        acc[referenceId] = {
          ...acc[referenceId],
          [overviewStatus]: [request],
        };
      }
    } else {
      acc[referenceId] = { [overviewStatus]: [request] };
    }

    return acc;
  }, {} as DocumentRequestReferenceGroup);

@Component({
  selector: 'bb-ac-document-request-viewer-widget',
  templateUrl: './ac-document-request-viewer-widget.component.html',
})
export class AcDocumentRequestViewerWidgetComponent implements OnInit {
  @Output() documentRequestId = new EventEmitter();
  readonly customer$: Observable<Customer | undefined> = this.acActionService.customer$;
  readonly requests$: Observable<DocumentRequestReferenceGroup> = this.acActionService.documentRequestList$.pipe(
    map((requests) => makeDrReferenceGroup(requests)),
  );
  readonly requestsError$: Observable<HttpErrorResponse> = this.acActionService.documentRequestListActions$.pipe(
    map((actions) => actions.loading.httpError),
    filter((x): x is HttpErrorResponse => !!x),
  );
  readonly requestsOrError$: Observable<DocumentRequestGroup | HttpErrorResponse> = merge(
    this.requests$,
    this.requestsError$,
  );
  readonly totalTasksCounter$: Observable<number> = this.acActionService.documentRequestList$.pipe(
    map((requests) => requests.filter((r) => Object.values(DocumentRequestStatus).includes(r.status)).length),
  );
  readonly activeTasksCounter$: Observable<number> = this.acActionService.documentRequestList$.pipe(
    map(
      (requests) =>
        requests.filter((r) => [DocumentRequestStatus.Open, DocumentRequestStatus.Rejected].includes(r.status)).length,
    ),
  );
  readonly loadingDocumentRequests$: Observable<boolean> = this.acActionService.documentRequestListActions$.pipe(
    pluck('loading'),
  );
  readonly documentRequestStatus = DocumentRequestStatus;
  readonly documentRequestOverviewGroup = DocumentRequestOverviewGroup;

  constructor(private readonly acActionService: AcActionService) {}

  ngOnInit() {
    this.acActionService.loadDocumentRequestList();
  }
}
