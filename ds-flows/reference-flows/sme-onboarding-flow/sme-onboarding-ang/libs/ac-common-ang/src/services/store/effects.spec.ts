import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { provideMockActions } from '@ngrx/effects/testing';
import { Action } from '@ngrx/store';
import { provideMockStore } from '@ngrx/store/testing';
import { cold, getTestScheduler, hot } from 'jasmine-marbles';
import { Observable, throwError } from 'rxjs';
import { createSpyObj } from '../../../../../createSpy';
import { ApplicationCenterStoreEffects } from './effects';

/**
 * Instance factories
 */
const anHttpResponseError = (message: string = 'httpError') => new HttpErrorResponse({ error: new Error(message) });
/**
 * Store mocks
 */
const initialState = {
  applicationCenter: {
    entities: {},
    actions: {},
  },
};
let actions$: Observable<Action>;

describe('Application Center store effects', () => {
  let interactionService: any;
  let effects: ApplicationCenterStoreEffects;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ApplicationCenterStoreEffects,
        provideMockStore({ initialState }),
        provideMockActions(() => actions$),
        {
          provide: FlowInteractionService,
          useValue: createSpyObj('flowInteractionService', ['call']),
        },
      ],
    });
    effects = TestBed.inject(ApplicationCenterStoreEffects);
    interactionService = TestBed.inject(FlowInteractionService) as FlowInteractionService;
  });

  describe('loadDocumentRequestListEffect$', () => {
    it('should dispatch the success action when request completes', () => {
      // given
      const expected = cold('--a', {
        a: {
          type: '[Application Center] Load Document Request List Success',
          documentRequestList: [],
          customer: { userId: 'someId', firstName: 'fName', lastName: 'lName', fullName: 'fName lName' },
        },
      });
      interactionService.call.mockReturnValue(
        cold('-a|', {
          a: {
            body: {
              documentRequests: [],
              customer: { userId: 'someId', firstName: 'fName', lastName: 'lName' },
            },
          },
        }),
      );
      // then
      actions$ = hot('-a-', {
        a: {
          type: '[Application Center] Load Document Request List',
        },
      });
      // expect
      expect(
        effects.loadDocumentRequestListEffect$({
          requestDelay: 10,
          scheduler: getTestScheduler(),
        }),
      ).toBeObservable(expected);
    });

    it('should dispatch the error action when request fails', () => {
      // given
      const httpError = anHttpResponseError('error');
      const expected = cold('--a', {
        a: {
          type: '[Application Center] Load Document Request List Error',
          httpError,
        },
      });
      interactionService.call.mockReturnValue(throwError(httpError));
      // then
      actions$ = hot('-a-', {
        a: {
          type: '[Application Center] Load Document Request List',
        },
      });
      // expect
      expect(
        effects.loadDocumentRequestListEffect$({
          requestDelay: 10,
          scheduler: getTestScheduler(),
        }),
      ).toBeObservable(expected);
    });
  });

  describe('loadDocumentRequest$', () => {
    it('should dispatch the success action when request completes', () => {
      // given
      const expected = cold('--a', {
        a: {
          type: '[Application Center] Load Document Request Success',
          internalId: 'someId',
          comments: [],
          fileset: { id: 'someId' },
        },
      });
      interactionService.call.mockReturnValue(
        cold('-a|', {
          a: {
            body: {
              comments: [],
              fileset: { id: 'someId' },
            },
          },
        }),
      );
      // then
      actions$ = hot('-a-', {
        a: {
          type: '[Application Center] Load Document Request',
          internalId: 'someId',
        },
      });
      // expect
      expect(
        effects.loadDocumentRequest$({
          requestDelay: 10,
          scheduler: getTestScheduler(),
        }),
      ).toBeObservable(expected);
    });

    it('should dispatch the error action when request fails', () => {
      // given
      const httpError = anHttpResponseError('error');
      const expected = cold('--a', {
        a: {
          type: '[Application Center] Load Document Request Error',
          httpError,
        },
      });
      interactionService.call.mockReturnValue(throwError(httpError));
      // then
      actions$ = hot('-a-', {
        a: {
          type: '[Application Center] Load Document Request',
          internalId: 'someId',
        },
      });
      // expect
      expect(
        effects.loadDocumentRequest$({
          requestDelay: 10,
          scheduler: getTestScheduler(),
        }),
      ).toBeObservable(expected);
    });
  });
});
