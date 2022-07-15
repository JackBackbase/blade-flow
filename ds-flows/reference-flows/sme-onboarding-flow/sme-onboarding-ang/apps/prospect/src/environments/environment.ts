import { createMocksInterceptor } from '@backbase/foundation-ang/data-http';
import { PortalContentMock } from '@backbase/foundation-ang/web-sdk';
import { Environment } from './type';
// @ts-ignore
import homeSimplePageModel from './simplified-models/home.json';

const pageModel = {
  ...homeSimplePageModel.children[0],
  label: 'Prospect Portal App',
  title: 'Prospect Portal App',
};

export const environment: Environment = {
  production: false,
  mockProviders: [createMocksInterceptor()],
  bootstrap: {
    pageModel,
    services: {
      portalContent: () => new PortalContentMock(),
    },
  },
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
