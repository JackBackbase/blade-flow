import { HttpErrorResponse } from '@angular/common/http';
import { Entities, Customer, Fileset, Comment } from '../../common/types';
import * as actions from './actions';
import * as acReducer from './reducer';
import * as acState from './state';

describe('Application center reducer', () => {
  const httpError = new HttpErrorResponse({ error: new Error('Some error from http') });

  describe('Load document request list actions', () => {
    describe('loadDocumentRequestList action', () => {
      it('should set loading action as in process', () => {
        const action = actions.loadDocumentRequestList();
        const state = acReducer.reducer(acState.initialState, action);

        expect(state.actions[Entities.DocumentRequestList].loading.processing).toBe(true);
      });
    });

    describe('loadDocumentRequestListSuccess action', () => {
      it('should set loading action as complete and successful', () => {
        const action = actions.loadDocumentRequestListSuccess({
          documentRequestList: [],
          customer: { userId: 'someId' } as Customer,
        });

        const state = acReducer.reducer(acState.initialState, action);

        expect(state.actions[Entities.DocumentRequestList].loading.success).toBe(true);
      });
    });

    describe('loadDocumentRequestListHttpError action', () => {
      it('should set httpError for the loading action', () => {
        const action = actions.loadDocumentRequestListHttpError({
          httpError,
        });

        const state = acReducer.reducer(acState.initialState, action);

        expect(state.actions[Entities.DocumentRequestList].loading.httpError).toBe(httpError);
      });
    });
  });

  describe('Load document request actions', () => {
    describe('loadDocumentRequest action', () => {
      it('should set loading action as in process', () => {
        const action = actions.loadDocumentRequest({ internalId: 'someId' });
        const state = acReducer.reducer(acState.initialState, action);

        expect(state.actions[Entities.DocumentRequest].loading.processing).toBe(true);
      });
    });

    describe('loadDocumentRequestSuccess action', () => {
      const action = actions.loadDocumentRequestSuccess({
        internalId: 'someId',
        fileset: { id: 'someId' } as Fileset,
        comments: [{ id: 'someId' }] as Comment[],
      });
      const state = acReducer.reducer(acState.initialState, action);

      it('should set loading action as complete and successful', () => {
        expect(state.actions[Entities.DocumentRequest].loading.success).toBe(true);
      });

      it('should set loaded fileset', () => {
        expect(state.entities[Entities.Fileset]).toEqual({ id: 'someId' } as Fileset);
      });

      it('should set loaded comments', () => {
        expect(state.entities[Entities.Comments]).toEqual([{ id: 'someId' }] as Comment[]);
      });
    });

    describe('loadDocumentRequestHttpError action', () => {
      it('should set httpError for the loading action', () => {
        const action = actions.loadDocumentRequestHttpError({
          httpError,
        });

        const state = acReducer.reducer(acState.initialState, action);

        expect(state.actions[Entities.DocumentRequest].loading.httpError).toBe(httpError);
      });
    });
  });
});
