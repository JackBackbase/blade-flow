import { Step } from '@backbase/ds-shared-ang/ui';

const PROSPECT_LABELS: Step[] = [
  {
    name: ['select-products'],
    label: $localize`:@@uso-nav.select-products.label:Saving account`,
    headline: $localize`:@@uso-nav.select-products.headline:Start saving your money`,
  },
  {
    name: ['anchor-data'],
    label: $localize`:@@uso-nav.anchor-data.label:Personal details`,
    headline: $localize`:@@uso-nav.anchor-data.headline:Nice to meet you`,
  },
  {
    name: ['otp-verification'],
    label: $localize`:@@uso-nav.otp-verification.label:Account verification`,
    headline: $localize`:@@uso-nav.otp-verification.headline:Let's secure your account`,
  },
  {
    name: ['identity-verification'],
    label: $localize`:@@uso-nav.identity-verification.label:ID protection`,
    headline: $localize`:@@uso-nav.identity-verification.headline:Smile for the camera`,
  },
  {
    name: ['address'],
    label: $localize`:@@uso-nav.address.label:Your address`,
    headline: $localize`:@@uso-nav.address.headline:Your address`,
  },
  {
    name: ['co-applicant'],
    label: $localize`:@@uso-nav.co-applicant.label:Applicants`,
    headline: $localize`:@@uso-nav.co-applicant.headline:Applicants`,
    children: [
      {
        name: ['co-applicant-data'],
        label: $localize`:@@uso-nav.co-applicant-data.label:Details`,
        headline: $localize`:@@uso-nav.co-applicant-data.headline:Co-applicant details`,
      },
      {
        name: ['co-applicant-address'],
        label: $localize`:@@uso-nav.co-applicant-address.label:Address`,
        headline: $localize`:@@uso-nav.co-applicant-address.headline:Co-applicant address`,
      },
    ],
  },
  {
    name: ['citizenship'],
    label: $localize`:@@uso-nav.citizenship.label:Citizenship`,
    headline: $localize`:@@uso-nav.citizenship.headline:Let’s verify your citizenship`,
    children: [
      {
        name: ['ssn', 'non-resident-data'],
        label: $localize`:@@uso-nav.ssn.label:Citizenship details`,
        headline: $localize`:@@uso-nav.ssn.headline:Let’s verify your citizenship`,
      },
    ],
  },

  {
    name: ['kyc'],
    label: $localize`:@@uso-nav.kyc.label:Additional information`,
    headline: $localize`:@@uso-nav.kyc.headline:Your occupation`,
  },
  {
    name: ['credentials'],
    label: $localize`:@@uso-nav.credentials.label:Create a password`,
    headline: $localize`:@@uso-nav.credentials.headline:Create a password`,
  },
];

const CO_APPLICANT_EXCLUDE_STEPS_LIST = ['co-applicant', 'select-products'];

let labels: Step[] = PROSPECT_LABELS;
if (sessionStorage.getItem('coApplicantId')) {
  CO_APPLICANT_EXCLUDE_STEPS_LIST.forEach(
    (excludeStep) => (labels = labels.filter((label) => !label.name.some((name) => excludeStep === name))),
  );
}

export { labels as stepperLabels };
