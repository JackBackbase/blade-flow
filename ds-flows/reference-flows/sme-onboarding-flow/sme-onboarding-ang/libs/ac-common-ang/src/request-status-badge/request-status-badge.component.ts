import { Component, Input, ChangeDetectionStrategy } from '@angular/core';
import { DocumentRequestStatus } from '../common/types';

@Component({
  selector: 'bb-request-status-badge',
  template: ` <bb-badge-ui [color]="color" [text]="text"></bb-badge-ui> `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class RequestStatusBadgeComponent {
  @Input() status!: DocumentRequestStatus;
  @Input() text = '';

  get color(): string {
    switch (this.status) {
      case DocumentRequestStatus.Open:
        return 'success';
      case DocumentRequestStatus.Pending:
        return 'warning';
      case DocumentRequestStatus.Approved:
        return 'success';
      case DocumentRequestStatus.Rejected:
        return 'danger';
      case DocumentRequestStatus.Cancelled:
        return 'danger';
      default:
        return 'info';
    }
  }
}
