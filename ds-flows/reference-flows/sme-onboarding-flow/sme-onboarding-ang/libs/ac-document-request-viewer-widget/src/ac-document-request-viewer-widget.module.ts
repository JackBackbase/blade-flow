import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import {
  HeaderModule,
  AvatarModule,
  BadgeModule,
  IconModule,
  LoadingIndicatorModule,
  BadgeCounterModule,
  NotificationModule,
  EmptyStateModule,
} from '@backbase/ui-ang';
import { ErrorHandlingUiModule } from '@backbase/case-management-ui-ang/ui';
import { AcCommonAngModule } from '@backbase/ac-common-ang';
import { DocumentRequestComponent } from './components/document-request.component';
import { AcDocumentRequestViewerWidgetComponent } from './ac-document-request-viewer-widget.component';

@NgModule({
  declarations: [AcDocumentRequestViewerWidgetComponent, DocumentRequestComponent],
  imports: [
    CommonModule,
    BackbaseCoreModule.withConfig({
      classMap: { AcDocumentRequestViewerWidgetComponent },
    }),
    AcCommonAngModule,
    HeaderModule,
    AvatarModule,
    BadgeModule,
    IconModule,
    LoadingIndicatorModule,
    BadgeCounterModule,
    NotificationModule,
    ErrorHandlingUiModule,
    EmptyStateModule,
    RouterModule.forChild([]),
  ],
  exports: [AcDocumentRequestViewerWidgetComponent],
})
export class AcDocumentRequestViewerWidgetModule {}
