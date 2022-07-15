import { createReducer, on, Action } from '@ngrx/store';
import { initialState, ApplicationCenterState } from './state';
import * as actions from './actions';
import { Entities, DocumentRequest } from '../../common/types';

const applicationCenterReducer = createReducer(
  initialState,
  /** Document Request List */
  on(actions.loadDocumentRequestList, (state) => ({
    ...state,
    actions: {
      ...state.actions,
      [Entities.DocumentRequestList]: {
        ...state.actions[Entities.DocumentRequestList],
        loading: {
          processing: true,
          httpError: undefined,
          success: false,
        },
      },
    },
  })),
  on(actions.loadDocumentRequestListSuccess, (state, { documentRequestList, customer }) => ({
    ...state,
    entities: {
      ...state.entities,
      [Entities.DocumentRequestList]: documentRequestList,
      [Entities.Customer]: customer,
    },
    actions: {
      ...state.actions,
      [Entities.DocumentRequestList]: {
        ...state.actions[Entities.DocumentRequestList],
        loading: {
          processing: false,
          httpError: undefined,
          success: true,
        },
      },
    },
  })),
  on(actions.loadDocumentRequestListHttpError, (state, { httpError }) => ({
    ...state,
    actions: {
      ...state.actions,
      [Entities.DocumentRequestList]: {
        ...state.actions[Entities.DocumentRequestList],
        loading: {
          processing: false,
          httpError,
          success: false,
        },
      },
    },
  })),
  /** Document Request */
  on(actions.loadDocumentRequest, (state) => ({
    ...state,
    actions: {
      ...state.actions,
      [Entities.DocumentRequest]: {
        ...state.actions[Entities.DocumentRequest],
        loading: {
          processing: true,
          httpError: undefined,
          success: false,
        },
      },
    },
  })),
  on(actions.loadDocumentRequestSuccess, (state, { comments, fileset, internalId }) => ({
    ...state,
    entities: {
      ...state.entities,
      [Entities.DocumentRequest]: state.entities[Entities.DocumentRequestList]
        ? (state.entities[Entities.DocumentRequestList].find((dr) => dr.internalId === internalId) as DocumentRequest)
        : undefined,
      [Entities.Fileset]: fileset,
      [Entities.Comments]: comments,
    },
    actions: {
      ...state.actions,
      [Entities.DocumentRequest]: {
        ...state.actions[Entities.DocumentRequest],
        loading: {
          processing: false,
          httpError: undefined,
          success: true,
        },
      },
    },
  })),
  on(actions.loadDocumentRequestHttpError, (state, { httpError }) => ({
    ...state,
    actions: {
      ...state.actions,
      [Entities.DocumentRequest]: {
        ...state.actions[Entities.DocumentRequest],
        loading: {
          processing: false,
          httpError,
          success: false,
        },
      },
    },
  })),
  // File Data reducer
  on(actions.deleteFile, (state, { fileId }) => ({
    ...state,
    actions: {
      ...state.actions,
      [Entities.FileData]: {
        ...state.actions[Entities.FileData],
        [fileId]: {
          ...state.actions[Entities.FileData][fileId],
          delete: {
            processing: true,
            httpError: undefined,
            success: false,
          },
        },
      },
    },
  })),
  on(actions.deleteFileSuccess, (state, { fileId, fileset }) => {
    return {
      ...state,
      entities: {
        ...state.entities,
        [Entities.Fileset]: fileset,
      },
      actions: {
        ...state.actions,
        [Entities.FileData]: {
          ...state.actions[Entities.FileData],
          [fileId]: {
            ...state.actions[Entities.FileData][fileId],
            delete: {
              processing: false,
              success: true,
              httpError: undefined,
            },
          },
        },
      },
    };
  }),
  on(actions.deleteFileHttpError, (state, { httpError, fileId }) => ({
    ...state,
    actions: {
      ...state.actions,
      [Entities.FileData]: {
        ...state.actions[Entities.FileData],
        [fileId]: {
          ...state.actions[Entities.FileData][fileId],
          delete: {
            processing: false,
            success: false,
            httpError,
          },
        },
      },
    },
  })),
);

export function reducer(state = initialState, action: Action): ApplicationCenterState {
  return applicationCenterReducer(state, action);
}
