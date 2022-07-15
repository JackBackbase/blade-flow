import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { LoadButtonModule, IconModule, ButtonModule, HeaderModule, InputCheckboxModule } from '@backbase/ui-ang';
import { ModalStepperModule, FormlyUiModule } from '@backbase/ds-shared-ang/ui';
import { DsAnchorJourneyAngComponent } from './ds-anchor-journey-ang.component';

const uiComps = [HeaderModule, LoadButtonModule, IconModule, ButtonModule, InputCheckboxModule];

@NgModule({
  imports: [
    CommonModule,
    ...uiComps,
    CdkStepperModule,
    ModalStepperModule,
    FormsModule,
    ReactiveFormsModule,
    FormlyUiModule,
    BackbaseCoreModule.withConfig({
      classMap: { DsAnchorJourneyAngComponent },
    }),
  ],
  declarations: [DsAnchorJourneyAngComponent],
  exports: [DsAnchorJourneyAngComponent],
})
export class DsAnchorJourneyAngModule {}
