import { Provider } from '@angular/core';
import { ExternalServices } from '@backbase/foundation-ang/start';

export interface Environment {
  readonly production: boolean;
  readonly mockProviders?: Array<Provider>;
  readonly bootstrap?: {
    readonly pageModel: any;
    readonly services: ExternalServices;
  };
}
