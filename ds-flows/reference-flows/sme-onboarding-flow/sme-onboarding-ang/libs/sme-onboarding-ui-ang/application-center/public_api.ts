import { NgModule } from '@angular/core';
import { AcCommonAngModule as AcCommonAngModuleWrapped } from '@backbase/ac-common-ang';
import { AcDocumentRequestViewerWidgetModule as AcDocumentRequestViewerWidgetModuleWrapped } from '@backbase/ac-document-request-viewer-widget';
import { AcHeaderWidgetModule as AcHeaderWidgetModuleWrapped } from '@backbase/ac-header-widget';
import { AcTaskDetailsViewerWidgetModule as AcTaskDetailsViewerWidgetModuleWrapped } from '@backbase/ac-task-details-viewer-widget';

@NgModule({
  imports: [AcCommonAngModuleWrapped],
  exports: [AcCommonAngModuleWrapped],
})
export class AcCommonAngModule {}

@NgModule({
  imports: [AcDocumentRequestViewerWidgetModuleWrapped],
  exports: [AcDocumentRequestViewerWidgetModuleWrapped],
})
export class AcDocumentRequestViewerWidgetModule {}
export { AcDocumentRequestViewerWidgetComponent } from '@backbase/ac-document-request-viewer-widget';

@NgModule({
  imports: [AcHeaderWidgetModuleWrapped],
  exports: [AcHeaderWidgetModuleWrapped],
})
export class AcHeaderWidgetModule {}
export { AcHeaderWidgetComponent } from '@backbase/ac-header-widget';

@NgModule({
  imports: [AcTaskDetailsViewerWidgetModuleWrapped],
  exports: [AcTaskDetailsViewerWidgetModuleWrapped],
})
export class AcTaskDetailsViewerWidgetModule {}
export { AcTaskDetailsViewerWidgetComponent } from '@backbase/ac-task-details-viewer-widget';
