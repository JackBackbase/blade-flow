import { ContentItem, ImageItem, Item, PortalContentMock } from '@backbase/foundation-ang/web-sdk';
import * as journeyInReviewDoneMessage from '../../sdlc/content/documents/journey-in-review-done-message.json';
import * as journeyRetryMessage from '../../sdlc/content/documents/journey-retry-message.json';
import * as journeySuccessfullyDoneMessage from '../../sdlc/content/documents/journey-successfully-done-message.json';
import * as journeySuccessfullyDoneCoApplicantMessage from '../../sdlc/content/documents/journey-successfully-done-co-applicant-message.json';
import * as prospectHome from '../../sdlc/bundles/simplified-model-prospect.json';
import * as customerHome from '../../sdlc/bundles/simplified-model-customer.json';
import * as coApplicantHome from '../../sdlc/bundles/simplified-model-co-applicant.json';

const getImage = (url: string) => {
  const imageItem: Partial<ImageItem> = {
    url: url,
  };
  return PortalContentMock.addStructuredContent<ImageItem>('backgroundImage', imageItem as ImageItem);
};

const getDocument = (documentItem: Partial<ContentItem<any>>) =>
  PortalContentMock.addStructuredContent<ContentItem<any>>('textDocument', documentItem as ContentItem<any>);

const getChildren = (simplifiedModel: any) => {
  if (!simplifiedModel) return;
  const steps = simplifiedModel.children[0].children;

  steps[0].children = steps[0].children.map(
    (step: {
      properties: { route: any; backgroundImageL: string; backgroundImageM: string; backgroundImageS: string };
      children: {
        properties: {
          textDocument: any;
          modalStepImage1: string;
          modalStepImage2: string;
          modalStepImage3: string;
          modalStepImage4: string;
          modalStepImage5: string;
          productImage: string;
          tncTermsConditionsLinkText: string;
          tncTermsConditionsLinkUrl: string;
          tncPrivacyStatementLinkText: string;
          tncPrivacyStatementLinkUrl: string;
        };
      }[];
    }) => {
      switch (step.properties.route) {
        case 'welcome':
          step.properties.backgroundImageL = getImage('assets/img-large-continue.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-continue.jpg');
          step.properties.backgroundImageS = getImage('assets/img-small-continue.jpg');
          step.children[0].properties.productImage = getImage('assets/img-small-continue.jpg');
          return step;
        case 'select-products':
          step.properties.backgroundImageL = getImage('assets/img-large-savingaccount.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-savingaccount.jpg');
          return step;
        case 'anchor-data':
        case 'co-applicant-data':
          step.properties.backgroundImageL = getImage('assets/img-large-step1.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step1.jpg');
          return step;
        case 'address':
        case 'co-applicant':
        case 'co-applicant-address':
          step.properties.backgroundImageL = getImage('assets/img-large-step2.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step2.jpg');
          return step;
        case 'ssn':
        case 'citizenship':
        case 'non-resident-data':
          step.properties.backgroundImageL = getImage('assets/img-large-step3.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step3.jpg');
          return step;
        case 'identity-verification':
          step.properties.backgroundImageL = getImage('assets/img-large-IDV.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-IDV.jpg');
          return step;
        case 'otp-verification':
          step.properties.backgroundImageL = getImage('assets/img-large-step4.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step4.jpg');
          return step;
        case 'credentials':
          step.properties.backgroundImageL = getImage('assets/img-large-step5.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step5.jpg');
          return step;
        case 'kyc':
          step.properties.backgroundImageL = getImage('assets/img-large-step6.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step6.jpg');
          return step;
        case 'terms-and-conditions':
          step.properties.backgroundImageL = getImage('assets/img-large-step1.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-step1.jpg');
          step.children[0].properties.modalStepImage1 = getImage('assets/img-walkthrough-1.jpg');
          step.children[0].properties.modalStepImage2 = getImage('assets/img-walkthrough-2.jpg');
          step.children[0].properties.modalStepImage3 = getImage('assets/img-walkthrough-3.jpg');
          step.children[0].properties.tncTermsConditionsLinkText = 'Terms and Conditions';
          step.children[0].properties.tncTermsConditionsLinkUrl = '';
          step.children[0].properties.tncPrivacyStatementLinkText = 'Privacy Statement';
          step.children[0].properties.tncPrivacyStatementLinkUrl = '';
          return step;
        case 'successfully-done':
          step.properties.backgroundImageL = getImage('assets/img-large-onboarded.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-onboarded.jpg');
          step.children[0].properties.textDocument = getDocument({ content: journeySuccessfullyDoneMessage.content });
          return step;
        case 'successfully-done-co-applicant':
          step.properties.backgroundImageL = getImage('assets/img-large-onboarded.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-onboarded.jpg');
          step.children[0].properties.textDocument = getDocument({
            content: journeySuccessfullyDoneCoApplicantMessage.content,
          });
          return step;
        case 'in-review-done':
          step.properties.backgroundImageL = getImage('assets/img-large-inreview.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-inreview.jpg');
          step.children[0].properties.textDocument = getDocument({ content: journeyInReviewDoneMessage.content });
          return step;
        case 'retry':
          step.properties.backgroundImageL = getImage('assets/img-large-retry.png');
          step.properties.backgroundImageM = getImage('assets/img-medium-retry.png');
          step.properties.backgroundImageS = getImage('assets/img-small-retry.png');
          step.children[0].properties.textDocument = getDocument({ content: journeyRetryMessage.content });
          return step;
        case 'declined':
          step.properties.backgroundImageL = getImage('assets/img-large-declined.jpg');
          step.properties.backgroundImageM = getImage('assets/img-medium-declined.jpg');
          return step;
        default:
          return step;
      }
    },
  );
  return steps;
};

const properties = {
  'render.requires': 'render-bb-ssr',
  'render.strategy': 'render-bb-widget-3',
  label: 'Interactions App Wc 3 Angular App Container',
  title: 'Interactions App Wc 3 Angular App Container',
  area: '0',
  order: 0,
};

export const pageModelProspect: Item = {
  name: 'bb-us-journey-ang-_-jufo0f',
  properties,
  children: getChildren(prospectHome as any),
};

export const pageModelCustomer: Item = {
  name: 'bb-us-journey-ang-_-jufo0f',
  properties,
  children: getChildren(customerHome as any),
};

export const pageModelCoApplicant: Item = {
  name: 'bb-us-journey-ang-_-jufo0f',
  properties,
  children: getChildren(coApplicantHome as any),
};
