import { ExternalServices } from '@backbase/foundation-ang/start';
import { PortalContentMock } from '@backbase/foundation-ang/web-sdk';

export const services: ExternalServices = {
  portalContent: () => new PortalContentMock(),
};
