import { Component } from '@angular/core';
import { InteractionDetailsView } from '@backbase/case-management-ui-ang/core';

@Component({
  selector: 'bb-ssn-interaction-tasks-form',
  template: ` <bb-ds-ssn-journey-ang [isIDT]="true"></bb-ds-ssn-journey-ang> `,
})
export class SsnInteractionTasksFormComponent extends InteractionDetailsView {
  constructor() {
    super();
  }
}
