import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { RouterModule } from '@angular/router';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { WebSdkApiModule } from '@backbase/foundation-ang/web-sdk';
import { FormlyFormBuilder } from '@ngx-formly/core';

//Core
import { FlowInteractionContainerModule, FlowInteractionCoreModule } from '@backbase/flow-interaction-sdk-ang/core';

// Journey Collection
import {
  OnoAnchorJourneyAngModule,
  OnoAddCoApplicantJourneyAngModule,
  OnoKycJourneyAngModule,
  OnoTacJourneyAngModule,
  OnoResponseDeclinedJourneyAngModule,
  OnoResponseJourneyAngModule,
  OnoResponseRetryJourneyAngModule,
  DsSsnJourneyAngModule,
  OnoLoadingWidgetAngModule,
  OnoCitizenshipSelectorWidgetAngModule,
  OnoNonResidentDataWidgetAngModule,
  OnoCoApplicantWelcomeJourneyAngModule,
} from '@backbase/ono-journey-collection-ang/features';

import { FormBuilder, OnoCommonAngModule } from '@backbase/ono-journey-collection-ang/common';

import {
  OnoOtpVerificationJourneyAngModule,
  OnoAddressJourneyAngModule,
  OnoIdvJumioJourneyAngModule,
  ProductSelectionJourneyAngModule,
} from '@backbase/ono-journey-collection-ang/journeys';

// Shared
import { FormlyUiModule } from '@backbase/ds-shared-ang/ui';
import { DsLayoutContainerAngModule } from '@backbase/ds-shared-ang/layout';

// Local

import { OnoCredentialsJourneyAngModule } from '@backbase/ono-credentials-journey-ang';
import { stepperLabels as labels } from './configs/ds-layout-container.config';
import { interactionConfig } from './configs/flow-interaction-core.config';
import { anchorDataConfig } from './configs/ono-anchor-journey.config';

const providers: any[] = environment.production ? [] : (environment.mockProviders as any);

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BackbaseCoreModule.forRoot({
      features: {
        THEME_V2: true,
      },
      assets: {
        assetsStaticItemName: environment.assetsStaticItemName,
      },
    }),
    WebSdkApiModule,
    RouterModule.forRoot([], { initialNavigation: 'disabled', useHash: true, relativeLinkResolution: 'legacy' }),
    FlowInteractionContainerModule,
    FlowInteractionCoreModule.forRoot(interactionConfig),
    OnoCommonAngModule,
    OnoAnchorJourneyAngModule.forRoot(anchorDataConfig),
    FormlyUiModule,
    OnoAddressJourneyAngModule,
    OnoAddCoApplicantJourneyAngModule,
    OnoOtpVerificationJourneyAngModule,
    OnoCredentialsJourneyAngModule,
    OnoKycJourneyAngModule,
    OnoTacJourneyAngModule,
    DsLayoutContainerAngModule.forRoot({
      onboarding: labels,
    }),
    OnoResponseJourneyAngModule,
    OnoIdvJumioJourneyAngModule,
    DsSsnJourneyAngModule,
    OnoResponseDeclinedJourneyAngModule,
    OnoResponseRetryJourneyAngModule,
    OnoCoApplicantWelcomeJourneyAngModule,
    OnoLoadingWidgetAngModule,
    ProductSelectionJourneyAngModule,
    OnoCitizenshipSelectorWidgetAngModule,
    OnoNonResidentDataWidgetAngModule,
  ],
  bootstrap: [AppComponent],
  providers: [
    ...providers,
    {
      provide: FormBuilder,
      useClass: FormlyFormBuilder,
    },
  ],
})
export class AppModule {}
