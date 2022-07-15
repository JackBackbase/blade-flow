import { CaseViewDefinition } from '@backbase/cm-case-data-overview-widget';

export const usoCaseDataViewDefinition: CaseViewDefinition = {
  header: {
    mainHeader: {
      viewHint: 'string',
      pointer: ['/mainApplicant/firstName', ' ', '/mainApplicant/lastName'],
    },
  },
  fieldsets: [
    {
      title: 'Personal information',
      fields: [
        {
          title: 'First name',
          viewHint: 'string',
          pointer: '/mainApplicant/firstName',
        },
        {
          title: 'Last name',
          viewHint: 'string',
          pointer: '/mainApplicant/lastName',
        },
        {
          title: 'Email address',
          viewHint: 'string',
          pointer: '/mainApplicant/email',
        },
        {
          title: 'Date of birth',
          viewHint: 'string',
          pointer: '/mainApplicant/dateOfBirth',
        },
        {
          title: 'Mobile phone',
          viewHint: 'string',
          pointer: '/mainApplicant/phoneNumber',
        },
      ],
    },
    {
      title: 'Address information',
      fields: [
        {
          title: 'Street name and number',
          viewHint: 'string',
          pointer: '/mainApplicant/address/numberAndStreet',
        },
        {
          title: 'Apt/suite',
          viewHint: 'string',
          pointer: '/mainApplicant/address/apt',
        },
        {
          title: 'City',
          viewHint: 'string',
          pointer: '/mainApplicant/address/city',
        },
        {
          title: 'State',
          viewHint: 'string',
          pointer: '/mainApplicant/address/state',
        },
        {
          title: 'Zip code',
          viewHint: 'string',
          pointer: '/mainApplicant/address/zipCode',
        },
      ],
    },
    {
      title: 'Identity',
      fields: [
        {
          title: 'SSN',
          viewHint: 'string',
          pointer: '/mainApplicant/citizenship/ssn',
        },
        {
          title: 'Foreign TIN',
          viewHint: 'string',
          pointer: '/mainApplicant/citizenship/foreignTin',
        },
        {
          title: 'National Tin',
          viewHint: 'string',
          pointer: '/mainApplicant/citizenship/nationalTin',
        },
      ],
    },
  ],
};
