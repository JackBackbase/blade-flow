import { Injectable } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import {
  ApplicationCenterState,
  DocumentRequestListActionsState,
  DocumentRequestActionsState,
  FileDataActionsState,
} from './store/state';
import { loadDocumentRequestList, loadDocumentRequest, deleteFile } from './store/actions';
import {
  customer,
  documentRequestList,
  documentRequestListActions,
  documentRequest,
  documentRequestActions,
  fileset,
  comments,
  fileDataActions,
  selectedDocumentRequest,
} from './store/selectors';
import { DocumentRequest, Customer, Fileset, Comment } from '../common/types';
import { InteractionActions } from '../common/constants';

@Injectable({
  providedIn: 'root',
})
export class AcActionService {
  initialized = false;
  readonly customer$: Observable<Customer | undefined> = this.store$.select(customer);
  readonly documentRequestList$: Observable<DocumentRequest[]> = this.store$.pipe(select(documentRequestList));
  readonly documentRequestListActions$: Observable<DocumentRequestListActionsState> = this.store$.pipe(
    select(documentRequestListActions),
  );
  readonly documentRequest$: Observable<DocumentRequest | undefined> = this.store$.pipe(select(documentRequest));
  readonly documentRequestActions$: Observable<DocumentRequestActionsState> = this.store$.pipe(
    select(documentRequestActions),
  );
  readonly fileset$: Observable<Fileset | undefined> = this.store$.pipe(select(fileset));
  readonly comments$: Observable<Comment[]> = this.store$.pipe(select(comments));
  readonly fileDataActions$: Observable<{
    [key: string]: FileDataActionsState;
  }> = this.store$.pipe(select(fileDataActions));
  readonly selectedDocumentRequest$: (internalId: string) => Observable<DocumentRequest | undefined> = (
    internalId: string,
  ) => this.store$.pipe(select(selectedDocumentRequest, { internalId }));

  constructor(
    private readonly store$: Store<ApplicationCenterState>,
    private readonly interactionService: FlowInteractionService,
  ) {}

  loadDocumentRequestList() {
    this.store$.dispatch(loadDocumentRequestList());
  }

  loadDocumentRequest(internalId: string) {
    this.store$.dispatch(loadDocumentRequest({ internalId }));
  }

  uploadFiles(files: File[], internalId: string, tempGroupId?: string) {
    return this.interactionService
      .postFiles(
        {
          action: InteractionActions.UPLOAD_DOCUMENT,
          body: {
            payload: {
              tempGroupId,
              internalId,
            },
          },
        },
        files,
      )
      .pipe(tap(() => this.store$.dispatch(loadDocumentRequest({ internalId }))));
  }

  downloadFile(fileId: string, internalId: string, tempGroupId?: string) {
    return this.interactionService
      .getFile(
        {
          action: InteractionActions.DOWNLOAD_DOCUMENT,
          body: {
            payload: {
              fileId,
              internalId,
              tempGroupId,
            },
          },
        },
        { httpHeaderAccept: 'application/octet-stream' },
      )
      .toPromise();
  }

  deleteFile(fileId: string, internalId: string, tempGroupId?: string) {
    this.store$.dispatch(deleteFile({ fileId, internalId, tempGroupId }));
  }

  completeTask(comment: string, internalId: string, tempGroupId?: string) {
    return this.interactionService
      .call({
        action: InteractionActions.COMPLETE_TASK,
        body: {
          payload: {
            internalId,
            tempGroupId,
            comment,
          },
        },
      })
      .toPromise();
  }
}
