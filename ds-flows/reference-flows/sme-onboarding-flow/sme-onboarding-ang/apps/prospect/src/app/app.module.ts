import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { DsBackgroundContainerModule } from '@backbase/ds-shared-ang/containers';
import { DsLayoutContainerAngModule } from '@backbase/ds-shared-ang/layout';
import { SmeOnboardingWalkthroughPagesModule } from '@backbase/ds-sme-onboarding-walkthrough-pages-ang';
import {
  FlowInteractionContainerModule,
  FlowInteractionCoreModule,
  ProtectRouteGuard,
} from '@backbase/flow-interaction-sdk-ang/core';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { WebSdkApiModule } from '@backbase/foundation-ang/web-sdk';
import { FormBuilder, OnoCommonAngModule } from '@backbase/ono-journey-collection-ang/common';
import {
  CompanyLookupJourneyAngModule,
  DocumentRequestJourneyAngModule,
  DsAddressJourneyAngModule,
  DsBusinessRelationsJourneyAngModule,
  DsSsnJourneyAngModule,
  OnoIdvJumioJourneyAngModule,
  OnoOtpVerificationJourneyAngModule,
  ProductSelectionJourneyAngModule,
} from '@backbase/ono-journey-collection-ang/journeys';
import {
  DsAnchorJourneyAngModule,
  DsCallActionAngModule,
  DsLandingJourneyAngModule,
} from '@backbase/sme-onboarding-ui-ang/journeys';
// 3rd party and backbase dependencies
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormlyFormBuilder, FormlyModule } from '@ngx-formly/core';
import { environment } from '../environments/environment';
import { AppComponent } from './app.component';
import { bundlesDefinition } from './bundles-definition';

@NgModule({
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    BackbaseCoreModule.forRoot({
      lazyModules: bundlesDefinition,
    }),
    BackbaseCoreModule.withConfig({
      guards: {
        canActivate: { ProtectRouteGuard },
      },
    }),
    FormlyModule.forRoot(),
    RouterModule.forRoot([], { initialNavigation: 'disabled', useHash: true }),
    WebSdkApiModule,
    FlowInteractionCoreModule.forRoot({
      steps: {
        'sme-onboarding-walkthrough': ['sme-onboarding-walkthrough'],
        'sme-onboarding-anchor': ['sme-onboarding-anchor'],
        'otp-verification': ['otp-verification'],
        'sme-onboarding-check-case': ['sme-onboarding-check-case'],
        'select-products': ['select-products'],
        'relation-type': ['business-relations', 'relation-type'],
        'update-current-user-control-person-step': ['business-relations', 'more-about-your-role'],
        'update-current-user-owner-step': ['business-relations', 'more-about-your-role'],
        'business-owners': ['business-relations', 'business-owners'],
        'control-persons': ['business-relations', 'control-persons'],
        'business-summary': ['business-relations', 'business-summary'],
        'business-structure': ['company-lookup', 'business-structure'],
        'document-request-journey': ['document-request-journey'],
        'company-lookup': ['company-lookup', 'company-lookup'],
        'business-details': ['company-lookup', 'business-details'],
        'business-address': ['company-lookup', 'business-address'],
        'business-identity': ['company-lookup', 'business-identity'],
        'identity-verified': ['identity-verified'],
        'identity-verification-initiation': ['identity-verification-initiation'],
        'check-business-relation-and-document-request': ['check-business-relation-and-document-request'],
        'sme-onboarding-personal-address': ['sme-onboarding-personal-address'],
        'sme-onboarding-ssn': ['sme-onboarding-ssn'],
        'sme-onboarding-landing': ['done'],
      },
      actionConfig: {
        'sme-onboarding-init': { updateCdo: true },
        'sme-onboarding-anchor-data': { updateCdo: true },
        'request-otp': { updateCdo: true },
        'verify-otp': { updateCdo: true },
        'sme-onboarding-check-case-exist': { updateCdo: true },
        'get-product-list': { updateCdo: true },
        'select-products': { updateCdo: true },
        'relation-type': { updateCdo: true },
        'update-current-user-control-person-step': { updateCdo: true },
        'update-current-user-owner-step': { updateCdo: true },
        'business-owners': { updateCdo: true },
        'control-persons': { updateCdo: true },
        'business-summary': { updateCdo: true },
        'business-structure': {},
        'company-lookup': {},
        'identity-verification-initiation': { updateCdo: false },
        'identity-verification-result': { updateCdo: false },
        'business-details': {},
        'business-address': { updateCdo: true },
        'business-identity': {},
        'load-document-requests': {},
        'load-document-request': {},
        'accept-terms-conditions': { updateCdo: true },
        'upload-document': {},
        'download-document': {},
        'delete-temp-document': {},
        'complete-task': { updateCdo: true },
        'submit-document-requests': { updateCdo: true },
        'sme-onboarding-personal-address': { updateCdo: true },
        'submit-ssn': { updateCdo: true },
        'sme-onboarding-landing-data': {},
        'check-business-relation-and-document-requests-conditions': {},
      },
    }),
    FlowInteractionContainerModule,
    SmeOnboardingWalkthroughPagesModule,
    DsAnchorJourneyAngModule,
    NgbModule,
    OnoCommonAngModule,
    DsLayoutContainerAngModule.forRoot({
      'sme-onboarding': [
        {
          label: 'Personal Details',
          name: ['sme-onboarding-anchor'],
          headline: '',
        },
        {
          label: 'Account verification',
          name: ['otp-verification'],
          headline: '',
        },
        {
          label: 'Choose product',
          name: ['select-products'],
          headline: 'Select your account type',
        },
        {
          label: 'Company lookup',
          name: ['your-business'],
          headline: '',
          children: [
            {
              label: 'Structure',
              name: ['business-structure'],
              headline: '',
            },
            {
              label: 'Find your business',
              name: ['company-lookup'],
              headline: '',
            },
            {
              label: 'Details',
              name: ['business-details'],
              headline: '',
            },
            {
              label: 'Address',
              name: ['business-address'],
              headline: '',
            },
            {
              label: 'Identity',
              name: ['business-identity'],
              headline: '',
            },
          ],
        },
        {
          label: 'Business Relations',
          name: ['business-relations'],
          headline: '',
          children: [
            {
              label: 'Your role',
              name: ['relation-type'],
              headline: '',
            },
            {
              label: 'More about your role as owner',
              name: ['update-current-user-owner-step'],
              headline: '',
            },
            {
              label: 'More about your role as control person',
              name: ['update-current-user-control-person-step'],
              headline: '',
            },
            {
              label: 'Business owners',
              name: ['business-owners'],
              headline: '',
            },
            {
              label: 'Control person',
              name: ['control-persons'],
              headline: '',
            },
            {
              label: 'Summary',
              name: ['business-summary'],
              headline: '',
            },
          ],
        },
        {
          label: 'Upload documents',
          name: ['document-request-journey'],
          headline: 'Let`s verify your business',
        },
        {
          label: 'ID protection',
          name: ['identity-verification-initiation'],
          headline: 'Fraud protection',
        },
        {
          label: 'Home address',
          name: ['sme-onboarding-personal-address'],
          headline: '',
        },
        {
          label: 'Your SSN',
          name: ['sme-onboarding-ssn'],
          headline: '',
        },
      ],
    }),
    OnoOtpVerificationJourneyAngModule,
    DsCallActionAngModule,
    DsAddressJourneyAngModule,
    DsSsnJourneyAngModule,
    DsLandingJourneyAngModule,
    DocumentRequestJourneyAngModule,
    ProductSelectionJourneyAngModule,
    DsBusinessRelationsJourneyAngModule,
    CompanyLookupJourneyAngModule,
    OnoIdvJumioJourneyAngModule,
    DsBackgroundContainerModule,
    RouterModule,
  ],
  declarations: [AppComponent],
  providers: [
    ...(environment.mockProviders || []),
    {
      provide: FormBuilder,
      useClass: FormlyFormBuilder,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
