import { Component, Input, OnChanges } from '@angular/core';
import { DocumentRequest, Customer, DocumentRequestStatus } from '@backbase/ac-common-ang';

@Component({
  selector: 'bb-document-request',
  templateUrl: './document-request.component.html',
  styleUrls: ['./document-request.component.scss'],
})
export class DocumentRequestComponent implements OnChanges {
  @Input() documentRequest!: DocumentRequest;
  @Input() assignee?: Customer;
  actionable = false;

  ngOnChanges() {
    const { status } = this.documentRequest;
    this.actionable = status === DocumentRequestStatus.Open || status === DocumentRequestStatus.Rejected;
  }
}
