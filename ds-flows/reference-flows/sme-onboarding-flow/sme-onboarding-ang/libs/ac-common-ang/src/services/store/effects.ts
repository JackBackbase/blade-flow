import { Injectable } from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Action } from '@ngrx/store';
import { asyncScheduler, combineLatest, Observable, of, timer } from 'rxjs';
import { catchError, map, mergeMap, pluck, switchMap } from 'rxjs/operators';
import { InteractionActions } from '../../common/constants';
import { Fileset } from '../../common/types';
import * as actions from './actions';

const REQUEST_DELAY = 400;

@Injectable()
export class ApplicationCenterStoreEffects {
  constructor(private readonly interactionService: FlowInteractionService, private readonly actions$: Actions) {}

  loadDocumentRequestListEffect$ = createEffect(
    () =>
      ({ requestDelay = REQUEST_DELAY, scheduler = asyncScheduler } = {}): Observable<Action> =>
        this.actions$.pipe(
          ofType(actions.loadDocumentRequestList),
          switchMap(() => {
            return combineLatest([
              timer(requestDelay, scheduler),
              this.interactionService.call({ action: InteractionActions.LOAD_DOCUMENT_REQUESTS, body: {} }).pipe(
                pluck('body'),
                map(({ documentRequests, customer }) => {
                  return actions.loadDocumentRequestListSuccess({
                    documentRequestList: documentRequests,
                    customer: {
                      ...customer,
                      fullName: customer.fullName || `${customer.firstName} ${customer.lastName}`,
                    },
                  });
                }),
                catchError((httpError) => of(actions.loadDocumentRequestListHttpError({ httpError }))),
              ),
            ]).pipe(map(([, result]) => result));
          }),
        ),
  );

  loadDocumentRequest$ = createEffect(
    () =>
      ({ requestDelay = REQUEST_DELAY, scheduler = asyncScheduler } = {}): Observable<Action> =>
        this.actions$.pipe(
          ofType(actions.loadDocumentRequest),
          switchMap((action) => {
            const { internalId } = action;

            return combineLatest([
              timer(requestDelay, scheduler),
              this.interactionService
                .call({
                  action: InteractionActions.LOAD_DOCUMENT_REQUEST,
                  body: {
                    payload: {
                      internalId,
                      tempGroupId: action.internalId,
                    },
                  },
                })
                .pipe(
                  pluck('body'),
                  map(({ comments, fileset }) => {
                    return actions.loadDocumentRequestSuccess({
                      internalId,
                      comments,
                      fileset,
                    });
                  }),
                  catchError((httpError) => of(actions.loadDocumentRequestHttpError({ httpError }))),
                ),
            ]).pipe(map(([, result]) => result));
          }),
        ),
  );

  deleteFile$ = createEffect(
    () => (): Observable<Action> =>
      this.actions$.pipe(
        ofType(actions.deleteFile),
        mergeMap((action) => {
          const { internalId, fileId, tempGroupId } = action;

          return this.interactionService
            .call({
              action: InteractionActions.DELETE_TEMP_DOCUMENT,
              body: {
                payload: {
                  fileId,
                  internalId,
                  tempGroupId,
                },
              },
            })
            .pipe(
              map((response) => response.body as Fileset),
              map((fileset: Fileset) => actions.deleteFileSuccess({ fileset, fileId })),
              catchError((httpError) => of(actions.deleteFileHttpError({ httpError, fileId }))),
            );
        }),
      ),
  );
}
