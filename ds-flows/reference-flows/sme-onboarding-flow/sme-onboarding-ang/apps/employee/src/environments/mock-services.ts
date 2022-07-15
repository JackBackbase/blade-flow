import { ExternalServices } from '@backbase/foundation-ang/start';

export const services: ExternalServices = {
  eventBus() {
    const subscriptions: { [key: string]: ((...args: any[]) => void)[] } = {};

    return {
      publish(eventName: string, data: any) {
        if (subscriptions[eventName]) {
          subscriptions[eventName].forEach((listener) => {
            listener(data);
          });
        }
      },
      subscribe(eventName: string, listener: (...args: any[]) => void) {
        subscriptions[eventName] = subscriptions[eventName] || [];
        subscriptions[eventName].push(listener);
      },
      unsubscribe(eventName: string, listener: (...args: any[]) => void) {
        const eventListeners = subscriptions[eventName];
        if (eventListeners) {
          eventListeners.splice(eventListeners.indexOf(listener), 1);
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
