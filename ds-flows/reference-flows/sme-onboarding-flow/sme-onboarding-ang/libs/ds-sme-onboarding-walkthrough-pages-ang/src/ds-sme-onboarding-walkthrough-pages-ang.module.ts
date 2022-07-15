import { CdkStepperModule } from '@angular/cdk/stepper';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ModalStepperModule } from '@backbase/ds-shared-ang/ui';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { HeaderModule, IconModule, InputCheckboxModule } from '@backbase/ui-ang';
import { SmeOnboardingWalkthroughPagesComponent } from './ds-sme-onboarding-walkthrough-pages-ang.component';
import { Step1Component } from './steps/step1/step1.component';
import { Step2Component } from './steps/step2/step2.component';
import { Step3Component } from './steps/step3/step3.component';
import { Step4Component } from './steps/step4/step4.component';
import { TermsAndConditionsStepComponent } from './steps/terms-and-conditions-step/terms-and-conditions-step.component';

const uiComps = [HeaderModule, IconModule, CdkStepperModule, ModalStepperModule, InputCheckboxModule];

const steps = [Step1Component, Step2Component, Step3Component, Step4Component, TermsAndConditionsStepComponent];

@NgModule({
  imports: [
    ...uiComps,
    CommonModule,
    ReactiveFormsModule,
    BackbaseCoreModule.withConfig({
      classMap: { SmeOnboardingWalkthroughPagesComponent },
    }),
  ],
  declarations: [...steps, SmeOnboardingWalkthroughPagesComponent],
})
export class SmeOnboardingWalkthroughPagesModule {}
