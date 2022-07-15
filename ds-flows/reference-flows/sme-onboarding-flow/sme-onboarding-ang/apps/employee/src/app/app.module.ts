import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { AddressValidationInteractionTasksModule } from '@backbase/address-validation-interactions-tasks';
import {
  CaseOverviewContainerModule,
  ModalOverlayContainerModule,
  PlatformContainerModule,
  ProcessOverviewContainerModule,
  SidebarOverlayContainerModule,
} from '@backbase/case-management-ui-ang/containers';
import { CoreAngModule } from '@backbase/case-management-ui-ang/core';
import { CustomSessionTimeoutComponent, CustomSessionTimeoutModule } from '@backbase/case-management-ui-ang/session';
import {
  ApplicantsListWidgetModule,
  ApplicantsViewDefinitionConfiguration,
  CaseDataOverviewWidgetModule,
  CaseFilesetListWidgetModule,
  CaseInfoWidgetModule,
  CaseOverviewListWidgetModule,
  CaseTaskListWidgetModule,
  CaseViewDefinitionConfiguration,
  CommentsWidgetModule,
  DmnInsightsWidgetModule,
  EventLogWidgetModule,
  EventsAndTasksWidgetModule,
  HeaderWidgetModule,
  InteractionsWithSteps,
  LiveInsightsWidgetModule,
  LogoutWidgetModule,
  ProcessDiagramSelectorWidgetModule,
  ProcessDiagramWidgetModule,
  ProcessInsightsCardWidgetModule,
  StatusTrackingWidgetModule,
  TaskCountSummaryWidgetModule,
  TaskDetailsWidgetModule,
  TaskOverviewListWidgetModule,
  UserDashboardWidgetModule,
  ViewInteractionTaskWidgetModule,
} from '@backbase/case-management-ui-ang/widgets';
import { CmAmlResultsWidgetModule } from '@backbase/cm-aml-results-widget';
import { CmBusinessRelationsResultsWidgetModule } from '@backbase/cm-business-relations-results-widget';
import { CmCreateBusinessCaseWidgetModule } from '@backbase/cm-create-business-case-widget';
import { CmRiskAssessmentResultsWidgetModule } from '@backbase/cm-risk-assessment-results-widget';
import { CompanyLookupInteractionTasksModule } from '@backbase/company-lookup-interaction-tasks';
import { TaskDetailsViewUiModule } from '@backbase/djm-task-details-view-ui';
import { DsBusinessRelationsInteractionTaskModule } from '@backbase/ds-business-relations-interaction-task';
import { FlowInteractionContainerModule } from '@backbase/flow-interaction-sdk-ang/core';
import { BackbaseCoreModule, SessionTimeoutModule } from '@backbase/foundation-ang/core';
import { WebSdkApiModule } from '@backbase/foundation-ang/web-sdk';
import { CmIdvResultsWidgetModule, CmInBranchIdentityVerificationModule } from '@backbase/ono-idv-jumio-journey-ang';
import { ContainersModule } from '@backbase/universal-ang/containers';
import { ContentWidgetModule } from '@backbase/universal-ang/content';
import { HeadingWidgetModule } from '@backbase/universal-ang/heading';
import { LayoutContainerModule } from '@backbase/universal-ang/layouts';
import { NavigationSpaWidgetModule } from '@backbase/universal-ang/navigation';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { SsnInteractionTasksModule } from 'libs/ssn-interactions-tasks/src/ssn-interaction-tasks.module';
import { ApplicantsViewDefinition } from '../assets/application-data-view-definition';
import { CaseDataViewDefinition } from '../assets/case-data-view-definition';
import { environment } from '../environments/environment.prod';
import { AppComponent } from './app.component';

const coreModules = [
  BrowserModule,
  BrowserAnimationsModule,
  BackbaseCoreModule.forRoot({
    features: {
      THEME_V2: true,
    },
  }),
  CustomSessionTimeoutModule,
  SessionTimeoutModule.forRoot({
    sessionTimeoutComponentClass: CustomSessionTimeoutComponent,
    inactivityModalTime: 60,
  }),
  WebSdkApiModule,
  CoreAngModule.forRoot(),
  RouterModule.forRoot([], { initialNavigation: 'disabled', useHash: true }),
  NgbModule,
];

const layoutModules = [
  ContainersModule,
  LayoutContainerModule,
  NavigationSpaWidgetModule,
  ContentWidgetModule,
  HeadingWidgetModule,
  FlowInteractionContainerModule,
];

const uiModules = [TaskDetailsViewUiModule];

const assetsPath =
  '/api/contentservices/api/contentstream/resourceRepository/contextRoot/static/items/bb-employee-ang/assets';

const idtSteps: InteractionsWithSteps = {
  'company-lookup-idt': [
    {
      label: 'Company Lookup',
      name: ['company-lookup'],
      headline: '',
      backgroundImages: {
        large: `${assetsPath}/img-large-address.png`,
        medium: `${assetsPath}/img-large-address.png`,
        small: '',
      },
      showStepper: true,
      children: [
        {
          label: 'Business structure',
          name: ['business-structure'],
          headline: '',
          backgroundImages: {
            large: `${assetsPath}/img-large-address.png`,
            medium: `${assetsPath}/img-large-address.png`,
            small: '',
          },
          showStepper: true,
        },
        {
          label: 'Find your business',
          name: ['company-lookup'],
          headline: '',
          backgroundImages: {
            large: `${assetsPath}/img-large-address.png`,
            medium: `${assetsPath}/img-large-address.png`,
            small: '',
          },
          showStepper: true,
        },
        {
          label: 'Business details',
          name: ['business-details'],
          headline: '',
          backgroundImages: {
            large: `${assetsPath}/img-large-address.png`,
            medium: `${assetsPath}/img-large-address.png`,
            small: '',
          },
          showStepper: true,
        },
        {
          label: 'Business address',
          name: ['business-address'],
          headline: '',
          backgroundImages: {
            large: `${assetsPath}/img-large-address.png`,
            medium: `${assetsPath}/img-large-address.png`,
            small: '',
          },
          showStepper: true,
        },
        {
          label: 'Business identity',
          name: ['business-identity'],
          headline: '',
          backgroundImages: {
            large: `${assetsPath}/img-large-address.png`,
            medium: `${assetsPath}/img-large-address.png`,
            small: '',
          },
          showStepper: true,
        },
      ],
    },
  ],
  'address-validation-idt': [
    {
      label: 'Home address',
      name: ['sme-onboarding-personal-address'],
      headline: 'Where do you live? ',
      backgroundImages: {
        large: `${assetsPath}/img-large-address.png`,
        medium: `${assetsPath}/img-large-address.png`,
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
        large: `${assetsPath}/img-large-fraud-protection.png`,
        medium: `${assetsPath}/img-large-fraud-protection.png`,
        small: '',
      },
      showStepper: true,
    },
  ],
  'ssn-idt': [
    {
      label: 'Your SSN',
      name: ['sme-onboarding-ssn'],
      headline: 'Let’s verify your identity',
      backgroundImages: {
        large: `${assetsPath}/img-large-fraud-protection.png`,
        medium: `${assetsPath}/img-large-fraud-protection.png`,
        small: '',
      },
      showStepper: true,
    },
  ],
};

const userModules = [
  ModalOverlayContainerModule,
  CaseOverviewListWidgetModule,
  CaseOverviewListWidgetModule,
  CaseInfoWidgetModule,
  HeaderWidgetModule,
  CaseTaskListWidgetModule,
  TaskDetailsWidgetModule,
  ProcessDiagramSelectorWidgetModule,
  TaskOverviewListWidgetModule,
  CaseFilesetListWidgetModule,
  EventLogWidgetModule,
  UserDashboardWidgetModule,
  ProcessDiagramSelectorWidgetModule,
  DmnInsightsWidgetModule,
  LiveInsightsWidgetModule,
  TaskCountSummaryWidgetModule,
  ProcessOverviewContainerModule,
  PlatformContainerModule,
  CaseOverviewContainerModule,
  SidebarOverlayContainerModule,
  ProcessInsightsCardWidgetModule,
  EventLogWidgetModule,
  EventsAndTasksWidgetModule,
  ProcessDiagramWidgetModule,
  CommentsWidgetModule,
  LogoutWidgetModule,
  CmAmlResultsWidgetModule,
  CaseDataOverviewWidgetModule,
  StatusTrackingWidgetModule,
  ApplicantsListWidgetModule,
  CmBusinessRelationsResultsWidgetModule,
  CmCreateBusinessCaseWidgetModule,
  CompanyLookupInteractionTasksModule,
  CmInBranchIdentityVerificationModule,
  ViewInteractionTaskWidgetModule.forRoot(idtSteps),
  DsBusinessRelationsInteractionTaskModule,
  CmRiskAssessmentResultsWidgetModule,
  AddressValidationInteractionTasksModule,
  SsnInteractionTasksModule,
  CmIdvResultsWidgetModule,
];

@NgModule({
  declarations: [AppComponent],
  imports: [...coreModules, ...layoutModules, ...userModules, ...uiModules],
  providers: [
    ...(environment.mockProviders || []),
    {
      provide: CaseViewDefinitionConfiguration,
      useValue: CaseDataViewDefinition,
    },
    {
      provide: ApplicantsViewDefinitionConfiguration,
      useValue: ApplicantsViewDefinition,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
