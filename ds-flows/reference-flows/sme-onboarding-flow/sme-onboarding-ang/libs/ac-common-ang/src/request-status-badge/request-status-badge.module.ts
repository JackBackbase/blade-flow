import { NgModule } from '@angular/core';
import { BadgeModule } from '@backbase/ui-ang';
import { RequestStatusBadgeComponent } from './request-status-badge.component';

@NgModule({
  declarations: [RequestStatusBadgeComponent],
  imports: [BadgeModule],
  exports: [RequestStatusBadgeComponent],
})
export class RequestStatusBadgeModule {}
