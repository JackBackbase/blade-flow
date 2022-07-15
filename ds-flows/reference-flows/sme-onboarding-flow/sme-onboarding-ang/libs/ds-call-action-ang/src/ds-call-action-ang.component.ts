import { Component, Input, OnInit } from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { FormHelperFactory } from '@backbase/ono-common-ang';

@Component({
  selector: 'bb-ds-call-action-ang',
  template: `
    <bb-loading-indicator-ui data-role="check-existing-case-loading" loaderSize="lg"></bb-loading-indicator-ui>
  `,
})
export class DsCallActionAngComponent implements OnInit {
  @Input() actionName?: string;

  constructor(private readonly interactionService: FlowInteractionService) {}

  ngOnInit() {
    if (!this.actionName) {
      console.error('"action" property must be set');

      return;
    }

    this.interactionService
      .call({
        action: this.actionName,
        body: {
          payload: {},
        },
      })
      .subscribe(() => {
        this.interactionService.nav.next();
      });
  }
}
