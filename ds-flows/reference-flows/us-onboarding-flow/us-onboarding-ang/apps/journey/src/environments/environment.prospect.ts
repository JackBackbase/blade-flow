import { PAGE_CONFIG } from '@backbase/foundation-ang/web-sdk';

import { Environment } from './type';
import { pageModelProspect as pageModel } from './_page-model';
import { services } from './_services';

export const environment: Environment = {
  production: false,
  mockProviders: [
    {
      provide: PAGE_CONFIG,
      useValue: {
        apiRoot: '/api/',
      },
    },
  ],
  bootstrap: {
    pageModel,
    services,
  },
  assetsStaticItemName: '',
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
