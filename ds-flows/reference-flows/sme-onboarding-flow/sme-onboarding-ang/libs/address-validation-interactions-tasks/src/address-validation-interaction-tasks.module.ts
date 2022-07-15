import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CoreAngModule } from '@backbase/case-management-ui-ang/core';
import { IdtTaskViewModule } from '@backbase/ds-shared-ang/ui';
import { AddressValidationInteractionTasksComponent } from './address-validation-interaction-tasks.component';
import { AddressValidationInteractionViewsModule } from './interaction-views/address-validation-interaction-views.module';

@NgModule({
  declarations: [AddressValidationInteractionTasksComponent],
  imports: [
    CommonModule,
    IdtTaskViewModule,
    AddressValidationInteractionViewsModule,
    CoreAngModule.withConfig({
      viewMap: {
        'address-validation-idt': AddressValidationInteractionTasksComponent,
      },
    }),
  ],
})
export class AddressValidationInteractionTasksModule {}
