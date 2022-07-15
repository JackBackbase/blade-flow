import { CaseViewDefinition } from '@backbase/case-management-ui-ang/widgets';

export const usoCaseDataViewApplicantsDefinition: CaseViewDefinition = {
  header: {
    mainHeader: {
      viewHint: 'string',
      pointer: ['/firstName', ' ', '/lastName'],
    },
  },
  fieldsets: [
    {
      title: 'Personal information',
      fields: [
        {
          title: 'First name',
          viewHint: 'string',
          pointer: '/firstName',
        },
        {
          title: 'Last name',
          viewHint: 'string',
          pointer: '/lastName',
        },
        {
          title: 'Email address',
          viewHint: 'string',
          pointer: '/email',
        },
        {
          title: 'Date of birth',
          viewHint: 'string',
          pointer: '/dateOfBirth',
        },
        {
          title: 'Mobile phone',
          viewHint: 'string',
          pointer: '/phoneNumber',
        },
      ],
    },
    {
      title: 'Address information',
      fields: [
        {
          title: 'Street name and number',
          viewHint: 'string',
          pointer: '/address/numberAndStreet',
        },
        {
          title: 'Apt/suite',
          viewHint: 'string',
          pointer: '/address/apt',
        },
        {
          title: 'City',
          viewHint: 'string',
          pointer: '/address/city',
        },
        {
          title: 'State',
          viewHint: 'string',
          pointer: '/address/state',
        },
        {
          title: 'Zip code',
          viewHint: 'string',
          pointer: '/address/zipCode',
        },
      ],
    },
    {
      title: 'Identity',
      fields: [
        {
          title: 'SSN',
          viewHint: 'string',
          pointer: '/citizenship/ssn',
        },
        {
          title: 'Foreign TIN',
          viewHint: 'string',
          pointer: '/citizenship/foreignTin',
        },
        {
          title: 'National Tin',
          viewHint: 'string',
          pointer: '/citizenship/nationalTin',
        },
      ],
    },
  ],
};
