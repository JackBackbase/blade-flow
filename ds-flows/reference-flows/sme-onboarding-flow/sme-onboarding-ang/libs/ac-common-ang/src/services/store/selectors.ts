import { createFeatureSelector, createSelector, MemoizedSelector, MemoizedSelectorWithProps } from '@ngrx/store';
import { Entities, DocumentRequest, Customer, Fileset, Comment } from '../../common/types';
import {
  ApplicationCenterState,
  DocumentRequestListActionsState,
  DocumentRequestActionsState,
  FileDataActionsState,
} from './state';

const applicationCenterState = createFeatureSelector<ApplicationCenterState>('applicationCenter');

export const customer: MemoizedSelector<ApplicationCenterState, Customer | undefined> = createSelector(
  applicationCenterState,
  (state: ApplicationCenterState) => state.entities[Entities.Customer],
);
export const documentRequestList: MemoizedSelector<ApplicationCenterState, DocumentRequest[]> = createSelector(
  applicationCenterState,
  (state: ApplicationCenterState) => state.entities[Entities.DocumentRequestList],
);
export const documentRequestListActions: MemoizedSelector<ApplicationCenterState, DocumentRequestListActionsState> =
  createSelector(
    applicationCenterState,
    (state: ApplicationCenterState) => state.actions[Entities.DocumentRequestList],
  );
export const documentRequest: MemoizedSelector<ApplicationCenterState, DocumentRequest | undefined> = createSelector(
  applicationCenterState,
  (state: ApplicationCenterState) => state.entities[Entities.DocumentRequest],
);
export const documentRequestActions: MemoizedSelector<ApplicationCenterState, DocumentRequestActionsState> =
  createSelector(applicationCenterState, (state: ApplicationCenterState) => state.actions[Entities.DocumentRequest]);
export const fileset: MemoizedSelector<ApplicationCenterState, Fileset | undefined> = createSelector(
  applicationCenterState,
  (state: ApplicationCenterState) => state.entities[Entities.Fileset],
);
export const comments: MemoizedSelector<ApplicationCenterState, Comment[]> = createSelector(
  applicationCenterState,
  (state: ApplicationCenterState) => state.entities[Entities.Comments],
);
export const fileDataActions: MemoizedSelector<
  ApplicationCenterState,
  | {
      [key: string]: FileDataActionsState;
    }
  | {}
> = createSelector(applicationCenterState, (state: ApplicationCenterState) => state.actions[Entities.FileData]);
export const selectedDocumentRequest: MemoizedSelectorWithProps<
  ApplicationCenterState,
  {
    internalId: string;
  },
  DocumentRequest | undefined
> = createSelector(applicationCenterState, (state: ApplicationCenterState, { internalId }: { internalId: string }) =>
  state.entities[Entities.DocumentRequestList].find((i) => i.internalId === internalId),
);
