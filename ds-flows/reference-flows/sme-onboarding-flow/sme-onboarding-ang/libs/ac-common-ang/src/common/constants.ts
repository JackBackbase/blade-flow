export const InteractionActions = {
  LOAD_DOCUMENT_REQUESTS: 'load-document-requests',
  LOAD_DOCUMENT_REQUEST: 'load-document-request',
  UPLOAD_DOCUMENT: 'upload-document',
  DOWNLOAD_DOCUMENT: 'download-document',
  DELETE_TEMP_DOCUMENT: 'delete-temp-document',
  MARK_DOCUMENT_FOR_DELETION: 'mark-document-for-deletion',
  COMPLETE_TASK: 'complete-task',
};

export enum FileStatus {
  Active = 'ACTIVE',
  ToBeAdded = 'TO_BE_ADDED',
  ToBeDeleted = 'TO_BE_DELETED',
  ToBeReplaced = 'TO_BE_REPLACED',
}
