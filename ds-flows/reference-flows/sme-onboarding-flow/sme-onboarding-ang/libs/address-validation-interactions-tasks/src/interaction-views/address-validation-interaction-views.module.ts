import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CoreAngModule } from '@backbase/cm-core-ang';
import { DsAddressJourneyAngModule } from '@backbase/ono-journey-collection-ang/journeys';
import { IconModule } from '@backbase/ui-ang';
import { AddressValidationInteractionTasksDoneComponent } from './address-validation-interaction-views-done.component';
import { AddressValidationInteractionTasksFormComponent } from './address-validation-interaction-views-form.component';

const viewModules = [DsAddressJourneyAngModule];

const viewComponents = [AddressValidationInteractionTasksFormComponent, AddressValidationInteractionTasksDoneComponent];

@NgModule({
  imports: [
    ...viewModules,
    CommonModule,
    IconModule,
    CoreAngModule.withConfig({
      viewMap: {
        'sme-onboarding-personal-address': AddressValidationInteractionTasksFormComponent,
        done: AddressValidationInteractionTasksDoneComponent,
      },
    }),
  ],
  declarations: [...viewComponents],
  exports: [...viewComponents],
})
export class AddressValidationInteractionViewsModule {}
