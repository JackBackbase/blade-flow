import { LazyConfig } from '@backbase/foundation-ang/core';

export const bundlesDefinition: LazyConfig = [
  // temporary solution to name as component, not as bundle classid name
  {
    module: 'DsBusinessRelationsJourneyAngModule',
    loadChildren: () =>
      import('../bundles/business-relations-bundle.module').then((m) => m.BusinessRelationsBundleModule),
  },
  {
    module: 'CompanyLookupJourneyAngModule',
    loadChildren: () => import('../bundles/company-lookup-bundle.module').then((m) => m.CompanyLookupBundleModule),
  },
  {
    module: 'DocumentRequestJourneyAngModule',
    loadChildren: () => import('../bundles/document-request-bundle.module').then((m) => m.DocumentRequestBundleModule),
  },
];
