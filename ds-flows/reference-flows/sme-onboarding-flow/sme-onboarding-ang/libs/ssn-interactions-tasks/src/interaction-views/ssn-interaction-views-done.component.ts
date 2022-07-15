import { Component, OnInit } from '@angular/core';
import { InteractionDetailsView } from '@backbase/case-management-ui-ang/core';

@Component({
  selector: 'bb-ssn-interaction-tasks-done',
  template: `
    <p>Done!</p>
    <p></p>
  `,
})
export class SsnInteractionTasksDoneComponent extends InteractionDetailsView implements OnInit {
  constructor() {
    super();
  }

  ngOnInit(): void {
    this.viewClose.emit();
  }
}
