import { ExternalServices } from '@backbase/foundation-ang/start';

export const normalizeNavigationTreeProperties = (navigationTree: any) => {
  const normalizedTree = {
    ...navigationTree,
    properties: Object.keys(navigationTree.preferences).reduce(
      (previousValue, currentValue) => ({
        ...previousValue,
        [currentValue]: navigationTree.preferences[currentValue].value,
      }),
      {},
    ),
  };
  delete normalizedTree.preferences;
  if (normalizedTree.children && Array.isArray(normalizedTree.children) && normalizedTree.children.length) {
    normalizedTree.children = normalizedTree.children.map((child: any) => normalizeNavigationTreeProperties(child));
  }
  return normalizedTree;
};

export const services: ExternalServices = {
  eventBus() {
    const subscriptions: { [key: string]: [(data: any) => any] } = {};
    return {
      publish: (eventName: string, data: any) => {
        if (subscriptions[eventName]) {
          subscriptions[eventName].forEach((listener) => {
            listener(data);
          });
        }
      },
      subscribe: (eventName: string, listener: (data: any) => any) => {
        subscriptions[eventName] = subscriptions[eventName] || [];
        subscriptions[eventName].push(listener);
      },
      unsubscribe: (eventName: string, listener: (data: any) => any) => {
        const eventListeners = subscriptions[eventName];
        if (eventListeners) {
          eventListeners.splice(eventListeners.findIndex(listener), 1);
        }
      },
    };
  },
  auth() {
    return {
      login: () => Promise.resolve(),
      logout: () => Promise.resolve(),
      goToLoginPage: () => Promise.resolve(),
      register: () => Promise.resolve(),
      refresh: () => Promise.resolve(),
      timeToLive: () => Promise.resolve(),
    };
  },
};
