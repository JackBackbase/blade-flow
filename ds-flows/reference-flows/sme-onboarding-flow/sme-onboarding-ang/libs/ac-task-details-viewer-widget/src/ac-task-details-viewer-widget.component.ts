import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Subject, Observable, combineLatest } from 'rxjs';
import { takeUntil, map, pluck, filter, switchMap } from 'rxjs/operators';
import { AcActionService, Customer, DocumentRequestStatus, DocumentRequest, Comment } from '@backbase/ac-common-ang';

@Component({
  selector: 'bb-ac-task-details-viewer-widget',
  templateUrl: './ac-task-details-viewer-widget.component.html',
})
export class AcTaskDetailsViewerWidgetComponent implements OnInit, OnDestroy {
  readonly documentRequestId$ = this.getRouteParam(this.route, 'documentRequestId');
  readonly request$: Observable<DocumentRequest | undefined> = this.documentRequestId$.pipe(
    filter<string | undefined, string>((id): id is string => id !== undefined),
    switchMap((id) => this.acActionService.selectedDocumentRequest$(id)),
  );
  readonly customer$: Observable<Customer | undefined> = this.acActionService.customer$;
  readonly comments$: Observable<Comment[]> = this.acActionService.comments$;
  readonly isActionable$: Observable<boolean> = this.request$.pipe(
    map(
      (request) => request?.status === DocumentRequestStatus.Open || request?.status === DocumentRequestStatus.Rejected,
    ),
  );
  readonly canCompleteTask$: Observable<boolean> = this.acActionService.fileset$.pipe(
    map((fileset) => Boolean(fileset && fileset.files.length)),
  );
  note = '';
  documentRequestId?: string;
  private readonly destroy$ = new Subject<void>();

  constructor(private readonly route: ActivatedRoute, private readonly acActionService: AcActionService) {}

  ngOnInit() {
    this.documentRequestId$
      .pipe(
        filter<string | undefined, string>((id): id is string => id !== undefined),
        takeUntil(this.destroy$),
      )
      .subscribe((id) => {
        this.documentRequestId = id;
        this.acActionService.loadDocumentRequest(id);
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  async completeTask() {
    if (!this.documentRequestId) {
      return;
    }

    await this.acActionService.completeTask(this.note, this.documentRequestId, this.documentRequestId);

    this.acActionService.loadDocumentRequest(this.documentRequestId);
    this.acActionService.loadDocumentRequestList();
  }

  onUpload(): void {
    if (!this.documentRequestId) {
      return;
    }

    this.acActionService.loadDocumentRequest(this.documentRequestId);
  }

  private getRouteParam(route: ActivatedRoute, param: string): Observable<string | undefined> {
    const paramValue = route.paramMap.pipe(pluck<ParamMap, string | undefined>('params', param));

    if (!route.parent) {
      return paramValue;
    }

    return combineLatest([paramValue, this.getRouteParam(route.parent, param)]).pipe(
      map(([childRoute, parentRoute]) => childRoute || parentRoute || undefined),
    );
  }
}
