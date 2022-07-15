import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import {
  BadgeModule,
  HeaderModule,
  IconModule,
  AvatarModule,
  LoadingIndicatorModule,
  TextareaModule,
  ButtonModule,
  TableModule,
  TooltipModule,
  LoadButtonModule,
  EmptyStateModule,
} from '@backbase/ui-ang';
import { FileUploadUiModule } from '@backbase/case-management-ui-ang/ui';
import { AcCommonAngModule } from '@backbase/ac-common-ang';
import { AcTaskDetailsViewerWidgetComponent } from './ac-task-details-viewer-widget.component';
import { CommentsComponent } from './components/comments/comments.component';
import { FilesetContainerComponent } from './components/fileset-container/fileset-container.component';
import { FilesetViewComponent } from './components/fileset-view/fileset-view.component';
import { FilesetUploadViewComponent } from './components/fileset-upload-view/fileset-upload-view.component';

@NgModule({
  declarations: [
    AcTaskDetailsViewerWidgetComponent,
    CommentsComponent,
    FilesetContainerComponent,
    FilesetViewComponent,
    FilesetUploadViewComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    BackbaseCoreModule.withConfig({
      classMap: { AcTaskDetailsViewerWidgetComponent },
    }),
    BadgeModule,
    HeaderModule,
    IconModule,
    AvatarModule,
    TableModule,
    IconModule,
    TooltipModule,
    FileUploadUiModule,
    AcCommonAngModule,
    LoadingIndicatorModule,
    TextareaModule,
    ButtonModule,
    LoadButtonModule,
    EmptyStateModule,
  ],
  exports: [AcTaskDetailsViewerWidgetComponent],
})
export class AcTaskDetailsViewerWidgetModule {}
