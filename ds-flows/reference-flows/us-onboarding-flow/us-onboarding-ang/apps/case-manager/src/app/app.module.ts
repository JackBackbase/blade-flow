import { AppComponent } from './app.component';
import { environment } from '../environments/environment';
import { usoCaseDataViewDefinition } from '../assets/uso-case-data-view-definition';
import { usoCaseDataViewApplicantsDefinition } from '../assets/uso-case-data-view-applicants-definition';
import { CaseDataMapperService } from './case-data-mapper.service';

// Angular
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

// Backbase
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { ContainersModule } from '@backbase/universal-ang/containers';
import { LayoutContainerModule } from '@backbase/universal-ang/layouts';
import { NavigationSpaWidgetModule } from '@backbase/universal-ang/navigation';
import { ContentWidgetModule } from '@backbase/universal-ang/content';
import { HeadingWidgetModule } from '@backbase/heading-widget-ang';
import { FormBuilder } from '@backbase/ono-journey-collection-ang/common';
import { WebSdkApiModule } from '@backbase/foundation-ang/web-sdk';
import { CmInBranchIdentityVerificationModule, CmIdvResultsWidgetModule } from '@backbase/ono-idv-jumio-journey-ang';

// 3rd party
import { FormlyFormBuilder } from '@ngx-formly/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// Journey Collection

import {
  CmNonResidentTaskWidgetModule,
  CmInBranchKycModule,
  CmInBranchAddressValidationModule,
  CmInBranchCitizenshipStatusModule,
} from '@backbase/ono-journey-collection-ang/features';

// Local

import { CmInBranchStartCaseAngModule } from '@backbase/cm-in-branch-start-case-ang';

// Case manager dependencies
import {
  CaseOverviewListWidgetModule,
  CaseInfoWidgetModule,
  CaseTaskListWidgetModule,
  ProcessDiagramWidgetModule,
  TaskOverviewListWidgetModule,
  HeaderWidgetModule,
  UserDashboardWidgetModule,
  LiveInsightsWidgetModule,
  TaskCountSummaryWidgetModule,
  DmnInsightsWidgetModule,
  ProcessDiagramSelectorWidgetModule,
  ProcessInsightsCardWidgetModule,
  EventLogWidgetModule,
  CommentsWidgetModule,
  TaskDetailsWidgetModule,
  CaseFilesetListWidgetModule,
  EventsAndTasksWidgetModule,
  ViewInteractionTaskWidgetModule,
  LogoutWidgetModule,
  CaseViewDefinitionConfiguration,
  ApplicantsViewDefinitionConfiguration,
  CaseDataOverviewWidgetModule,
  ApplicantsListWidgetModule,
  InteractionsWithSteps,
} from '@backbase/case-management-ui-ang/widgets';
import {
  PlatformContainerModule,
  ProcessOverviewContainerModule,
  CaseOverviewContainerModule,
  SidebarOverlayContainerModule,
  ModalOverlayContainerModule,
} from '@backbase/case-management-ui-ang/containers';
import { CASE_DATA_MAPPER, CoreAngModule } from '@backbase/case-management-ui-ang/core';
import { CmAmlResultsWidgetModule } from '@backbase/cm-aml-results-widget';

const providers: any[] = [
  environment.production ? [] : (environment.mockProviders as any),
  {
    provide: CaseViewDefinitionConfiguration,
    useValue: usoCaseDataViewDefinition,
  },
  {
    provide: ApplicantsViewDefinitionConfiguration,
    useValue: usoCaseDataViewApplicantsDefinition,
  },
  {
    provide: CASE_DATA_MAPPER,
    useClass: CaseDataMapperService,
  },
];

const layoutModules = [
  ContainersModule,
  LayoutContainerModule,
  NavigationSpaWidgetModule,
  ContentWidgetModule,
  HeadingWidgetModule,
];

const assetsPath =
  '/api/contentservices/api/contentstream/resourceRepository/contextRoot/static/items/bb-us-case-manager-ang/assets';

const idtSteps: InteractionsWithSteps = {
  'address-validation-idt': [
    {
      label: 'Your address',
      name: ['address'],
      headline: 'Where do you live?',
      backgroundImages: {
        large: `${assetsPath}/img-large-step2.jpg`,
        medium: `${assetsPath}/img-medium-step2.jpg`,
        small: '',
      },
      showStepper: true,
    },
  ],
  'idv-idt': [
    {
      label: 'ID protection',
      name: ['identity-verification'],
      headline: 'Smile for the camera',
      backgroundImages: {
        large: `${assetsPath}/img-large-IDV.jpg`,
        medium: `${assetsPath}/img-medium-IDV.jpg`,
        small: '',
      },
      showStepper: true,
    },
  ],
  'kyc-idt': [
    {
      label: 'Additional information',
      name: ['kyc'],
      headline: 'Tell us a little more about yourself',
      backgroundImages: {
        large: `${assetsPath}/img-large-additionalinfo.jpg`,
        medium: `${assetsPath}/img-medium-additionalinfo.jpg`,
        small: '',
      },
      showStepper: true,
    },
  ],
  'citizenship-idt': [
    {
      label: 'Citizenship',
      name: ['citizenship'],
      headline: "Let's verify your citizenship",
      backgroundImages: {
        large: `${assetsPath}/img-large-step3.jpg`,
        medium: `${assetsPath}/img-medium-step3.jpg`,
        small: '',
      },
      showStepper: true,
      children: [
        {
          label: 'Citizenship details',
          name: ['ssn', 'non-resident-data'],
          headline: 'Let’s verify your citizenship',
          backgroundImages: {
            large: `${assetsPath}/img-large-step3.jpg`,
            medium: `${assetsPath}/img-medium-step3.jpg`,
            small: '',
          },
          showStepper: true,
        },
      ],
    },
  ],
};

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    BackbaseCoreModule.forRoot({
      features: {
        THEME_V2: true,
      },
    }),
    RouterModule.forRoot([], { initialNavigation: 'disabled', useHash: true, relativeLinkResolution: 'legacy' }),
    CoreAngModule.forRoot(),
    NgbModule,
    WebSdkApiModule,
    CaseOverviewListWidgetModule,
    CaseInfoWidgetModule,
    CaseTaskListWidgetModule,
    ProcessDiagramWidgetModule,
    TaskOverviewListWidgetModule,
    EventLogWidgetModule,
    CommentsWidgetModule,
    UserDashboardWidgetModule,
    TaskCountSummaryWidgetModule,
    DmnInsightsWidgetModule,
    PlatformContainerModule,
    LiveInsightsWidgetModule,
    CaseOverviewContainerModule,
    ModalOverlayContainerModule,
    ProcessInsightsCardWidgetModule,
    ProcessOverviewContainerModule,
    ProcessDiagramSelectorWidgetModule,
    CmAmlResultsWidgetModule,
    CmIdvResultsWidgetModule,
    CmNonResidentTaskWidgetModule,
    HeaderWidgetModule,
    SidebarOverlayContainerModule,
    TaskDetailsWidgetModule,
    CaseFilesetListWidgetModule,
    EventsAndTasksWidgetModule,
    LogoutWidgetModule,
    CaseDataOverviewWidgetModule,
    ApplicantsListWidgetModule,
    ...layoutModules,
    CmInBranchStartCaseAngModule,
    CmInBranchAddressValidationModule,
    CmInBranchCitizenshipStatusModule,
    CmInBranchIdentityVerificationModule,
    CmInBranchKycModule,
    ViewInteractionTaskWidgetModule.forRoot(idtSteps),
  ],
  providers: [
    ...providers,
    {
      provide: FormBuilder,
      useClass: FormlyFormBuilder,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
