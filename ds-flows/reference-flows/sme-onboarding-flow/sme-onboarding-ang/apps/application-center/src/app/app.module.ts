import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { WebSdkApiModule } from '@backbase/foundation-ang/web-sdk';
import { ContainersModule } from '@backbase/universal-ang/containers';
import { HeadingWidgetModule } from '@backbase/universal-ang/heading';
import { FlowInteractionCoreModule, FlowInteractionContainerModule } from '@backbase/flow-interaction-sdk-ang/core';
import { environment } from '../environments/environment';
import { OnoOtpVerificationJourneyAngModule } from '@backbase/ono-journey-collection-ang/journeys';
import { DsBackgroundContainerModule } from '@backbase/ds-shared-ang/containers';
import { SidebarOverlayContainerModule } from '@backbase/case-management-ui-ang/containers';
import { DsStatusTrackingWidgetModule } from '@backbase/ds-shared-ang/widgets';
import { DsInteractionInitAngModule } from '@backbase/sme-onboarding-ui-ang/misc';
import {
  AcCommonAngModule,
  AcDocumentRequestViewerWidgetModule,
  AcHeaderWidgetModule,
  AcTaskDetailsViewerWidgetModule,
} from '@backbase/sme-onboarding-ui-ang/application-center';

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    BackbaseCoreModule.forRoot({
      features: {
        THEME_V2: true,
      },
      assets: {
        assetsStaticItemName: environment.assetsStaticItemName,
      },
    }),
    RouterModule.forRoot([], { initialNavigation: 'disabled', useHash: true }),
    WebSdkApiModule,
    ContainersModule,
    OnoOtpVerificationJourneyAngModule,
    DsInteractionInitAngModule,
    DsBackgroundContainerModule,
    DsStatusTrackingWidgetModule,
    AcHeaderWidgetModule,
    AcCommonAngModule,
    AcDocumentRequestViewerWidgetModule,
    AcTaskDetailsViewerWidgetModule,
    SidebarOverlayContainerModule,
    HeadingWidgetModule,
    FlowInteractionCoreModule.forRoot({
      actionConfig: {
        'application-center-init': {},
        'request-otp': { updateCdo: true },
        'verify-otp': { updateCdo: true },
        'load-document-requests': {},
        'download-document': {},
      },
      steps: {
        'application-center-init': ['application-center-init'],
        'otp-verification': ['otp-verification'],
        'sme-application-center': ['sme-application-center'],
      },
    }),
    FlowInteractionContainerModule,
  ],
  providers: [...(environment.mockProviders || [])],
  bootstrap: [AppComponent],
})
export class AppModule {}
