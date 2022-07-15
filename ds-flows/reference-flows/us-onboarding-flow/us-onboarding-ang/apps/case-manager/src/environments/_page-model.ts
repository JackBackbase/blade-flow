import { Item } from '@backbase/foundation-ang/web-sdk';
import * as home from '../../sdlc/bundles/simplified-model.json';

export const pageModel: Item = {
  name: 'bb-us-case-manager-ang-_-6czon',
  properties: {
    'render.requires': 'render-bb-ssr',
    'render.strategy': 'render-bb-widget-3',
    label: 'Case Management App Wc 3 Angular App Container',
    title: 'Case Management App Wc 3 Angular App Container',
    area: '0',
    order: 0,
  },
  children: (home as any).children[0].children,
};
