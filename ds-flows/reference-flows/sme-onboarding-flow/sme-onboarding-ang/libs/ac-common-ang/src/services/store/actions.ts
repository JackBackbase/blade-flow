import { HttpErrorResponse } from '@angular/common/http';
import { createAction, props } from '@ngrx/store';
import { DocumentRequest, Customer, Comment, Fileset } from '../../common/types';

export const loadDocumentRequestList = createAction('[Application Center] Load Document Request List');

export const loadDocumentRequestListSuccess = createAction(
  '[Application Center] Load Document Request List Success',
  props<{
    documentRequestList: DocumentRequest[];
    customer: Customer;
  }>(),
);

export const loadDocumentRequestListHttpError = createAction(
  '[Application Center] Load Document Request List Error',
  props<{ httpError: HttpErrorResponse }>(),
);

export const loadDocumentRequest = createAction(
  '[Application Center] Load Document Request',
  props<{ internalId: string }>(),
);

export const loadDocumentRequestSuccess = createAction(
  '[Application Center] Load Document Request Success',
  props<{
    internalId: string;
    comments: Comment[];
    fileset: Fileset;
  }>(),
);

export const loadDocumentRequestHttpError = createAction(
  '[Application Center] Load Document Request Error',
  props<{ httpError: HttpErrorResponse }>(),
);

export const deleteFile = createAction(
  '[Application Center] Delete File',
  props<{ fileId: string; internalId: string; tempGroupId?: string }>(),
);

export const deleteFileSuccess = createAction(
  '[Application Center] Delete File Success',
  props<{ fileId: string; fileset: Fileset }>(),
);

export const deleteFileHttpError = createAction(
  '[Application Center] Delete File Http Error',
  props<{ fileId: string; httpError: HttpErrorResponse }>(),
);
