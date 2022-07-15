import { createMocksInterceptor } from '@backbase/foundation-ang/data-http';
import { Environment } from './type';
// @ts-ignore
import homeSimplePageModel from './simplified-models/home.json';
import { services } from './mock-services';

const pageModel = {
  ...homeSimplePageModel.children[0],
  label: 'SME Onboarding Employee app',
  title: 'SME Onboarding Employee app',
};

export const environment: Environment = {
  production: false,
  mockProviders: [createMocksInterceptor()],
  bootstrap: {
    pageModel,
    services,
  } as any,
};
