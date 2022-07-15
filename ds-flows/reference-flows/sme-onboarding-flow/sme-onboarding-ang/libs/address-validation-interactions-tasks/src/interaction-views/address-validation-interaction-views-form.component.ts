import { Component } from '@angular/core';
import { InteractionDetailsView } from '@backbase/case-management-ui-ang/core';

@Component({
  selector: 'bb-address-validation-interaction-tasks-form',
  template: `
    <bb-ds-address-journey-ang
      [showCheckbox]="true"
      [shouldFetchAddress]="true"
      addressName="Personal"
      asAnotherAddressCheckboxText="Same as my business address"
      fetchedAddressType="Business"
    ></bb-ds-address-journey-ang>
  `,
})
export class AddressValidationInteractionTasksFormComponent extends InteractionDetailsView {
  constructor() {
    super();
  }
}
