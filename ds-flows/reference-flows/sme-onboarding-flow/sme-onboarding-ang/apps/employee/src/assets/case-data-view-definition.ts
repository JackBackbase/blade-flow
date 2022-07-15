import { CaseViewDefinition } from '@backbase/case-management-ui-ang/widgets';

export const CaseDataViewDefinition: CaseViewDefinition = {
  header: {
    mainHeader: {
      viewHint: 'string',
      pointer: '/companyLookupInfo/businessDetailsInfo/legalName',
    },
    subHeader: {
      viewHint: 'string',
      pointer: '/companyLookupInfo/businessDetailsInfo/legalName',
    },
  },
  fieldsets: [
    {
      title: 'Business Information',
      fields: [
        {
          title: 'Legal name',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessDetailsInfo/legalName',
        },
        {
          title: 'Known name',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessDetailsInfo/dba',
        },

        {
          title: 'Business type',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessDetailsInfo/businessStructureInfo/type',
        },
        {
          title: 'Date Established',
          viewHint: 'date',
          pointer: '/companyLookupInfo/businessDetailsInfo/dateEstablished',
        },
        {
          title: 'EIN',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessDetailsInfo/ein',
        },
        {
          title: 'State Operating',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessDetailsInfo/stateOperatingIn',
        },
      ],
    },
    {
      title: 'Business Address',
      fields: [
        {
          title: 'Street',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessAddressInfo/numberAndStreet',
        },
        {
          title: 'Apt / Suite',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessAddressInfo/apt',
        },
        {
          title: 'City',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessAddressInfo/city',
        },
        {
          title: 'State',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessAddressInfo/state',
        },
        {
          title: 'Zip code',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessAddressInfo/zipCode',
        },
      ],
    },
    {
      title: 'Business Identity',
      fields: [
        {
          title: 'Industry',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessIdentityInfo/industry/description',
        },
        {
          title: 'Business Description',
          viewHint: 'string',
          pointer: '/companyLookupInfo/businessIdentityInfo/description',
        },
        {
          title: 'Company website',
          viewHint: 'link',
          pointer: '/companyLookupInfo/businessIdentityInfo/website',
        },
      ],
    },
  ],
};
