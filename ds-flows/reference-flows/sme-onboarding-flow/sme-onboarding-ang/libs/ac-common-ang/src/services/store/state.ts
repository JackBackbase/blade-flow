import { HttpErrorResponse } from '@angular/common/http';
import { DocumentRequest, Entities, Customer, Comment, Fileset } from '../../common/types';

export enum ActionNames {
  Loading = 'loading',
  Download = 'download',
  Delete = 'delete',
  Upload = 'upload',
}

export interface ActionStatus {
  processing: boolean;
  httpError: HttpErrorResponse | undefined;
  success: boolean;
}

export type DocumentRequestListActionsState = Record<ActionNames.Loading, ActionStatus>;
export type DocumentRequestActionsState = Record<ActionNames.Loading, ActionStatus>;
export type FileDataActionsState = Record<ActionNames.Download | ActionNames.Delete | ActionNames.Upload, ActionStatus>;

export interface ApplicationCenterState {
  entities: {
    [Entities.Customer]: Customer | undefined;
    [Entities.DocumentRequestList]: DocumentRequest[];
    [Entities.DocumentRequest]: DocumentRequest | undefined;
    [Entities.Fileset]: Fileset | undefined;
    [Entities.Comments]: Comment[];
  };
  actions: {
    [Entities.DocumentRequestList]: DocumentRequestListActionsState;
    [Entities.DocumentRequest]: DocumentRequestActionsState;
    [Entities.FileData]: {
      [key: string]: FileDataActionsState;
    };
  };
}

export const initialState: ApplicationCenterState = {
  entities: {
    [Entities.Customer]: undefined,
    [Entities.DocumentRequestList]: [],
    [Entities.DocumentRequest]: undefined,
    [Entities.Fileset]: undefined,
    [Entities.Comments]: [],
  },
  actions: {
    [Entities.DocumentRequestList]: {
      [ActionNames.Loading]: {
        processing: false,
        httpError: undefined,
        success: false,
      },
    },
    [Entities.DocumentRequest]: {
      [ActionNames.Loading]: {
        processing: false,
        httpError: undefined,
        success: false,
      },
    },
    [Entities.FileData]: {},
  },
};
