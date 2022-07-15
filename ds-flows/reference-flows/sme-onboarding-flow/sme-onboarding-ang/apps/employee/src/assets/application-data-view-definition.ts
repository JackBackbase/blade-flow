import { CaseViewDefinition } from '@backbase/case-management-ui-ang/widgets';

export const ApplicantsViewDefinition: CaseViewDefinition = {
  header: {
    mainHeader: {
      viewHint: 'string',
      pointer: ['/firstName', ' ', '/lastName'],
    },
    subHeader: {
      viewHint: 'string',
      pointer: ['/role', ', ', '/ownership_percentage', '% ownership'],
    },
  },
  fieldsets: [
    {
      title: 'Personal Information',
      fields: [
        {
          title: 'First Name',
          viewHint: 'string',
          pointer: '/firstName',
        },
        {
          title: 'Last Name',
          viewHint: 'string',
          pointer: '/lastName',
        },
        {
          title: 'Email',
          viewHint: 'string',
          pointer: '/email',
        },
        {
          title: 'Phone number',
          viewHint: 'string',
          pointer: '/phoneNumber',
        },
        {
          title: 'SSN',
          viewHint: 'string',
          pointer: '/ssn',
        },
        {
          title: 'Control person',
          viewHint: 'boolean',
          pointer: '/control_person',
        },
      ],
    },
    {
      title: 'Personal Address',
      fields: [
        {
          title: 'Street number & name',
          viewHint: 'string',
          pointer: '/personalAddress/numberAndStreet',
        },
        {
          title: 'Apt/Suite',
          viewHint: 'string',
          pointer: '/personalAddress/apt',
        },
        {
          title: 'City',
          viewHint: 'string',
          pointer: '/personalAddress/city',
        },
        {
          title: 'State',
          viewHint: 'string',
          pointer: '/personalAddress/state',
        },
        {
          title: 'Zip code',
          viewHint: 'string',
          pointer: '/personalAddress/zipCode',
        },
      ],
    },
  ],
};
