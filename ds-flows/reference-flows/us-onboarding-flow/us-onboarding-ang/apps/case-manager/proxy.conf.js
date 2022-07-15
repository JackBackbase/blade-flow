const anonymousUserIdCookieName = 'anonymousUserId';
const cookieHeaderTrackingName = 'newcookie' + Math.floor(Math.random() * 1e6);
const randomString = () =>
  Math.random()
    .toString(36)
    .substring(2, 15);
const addCookie = (name, value, cookies = '') => `${name}=${value}; ${cookies}`;
const getCookieValue = (cookies, name) => {
  const [, valueAndRest = ''] = `; ${cookies}`.split(`; ${name}=`);
  return valueAndRest.split(';')[0];
};

const PROXY_CONFIG = {
  '/api/portal/static/items/bb-us-case-manager-ang/assets': {
    target: 'http://localhost:4202',
    secure: false,
    pathRewrite: {
      '/api/portal/static/items/bb-us-case-manager-ang/assets/': 'assets/',
    },
    logLevel: 'debug',
  },
  '/api/contentservices/api/contentstream/resourceRepository/contextRoot/static/items/bb-us-case-manager-ang/assets': {
    target: 'http://localhost:4202',
    secure: false,
    pathRewrite: {
      '/api/contentservices/api/contentstream/resourceRepository/contextRoot/static/items/bb-us-case-manager-ang/assets/': 'assets/',
    },
    logLevel: 'debug',
  },
  '/gateway/api': {
    target: 'http://localhost:8080/gateway/api',
    secure: false,
    pathRewrite: {
      '^/gateway/api': '',
    },
    onProxyReq: function(proxyReq, req, res) {
      const anonymousUserId = getCookieValue(req.headers.cookie, anonymousUserIdCookieName);
      if (!anonymousUserId) {
        const newCookieValue = randomString();
        proxyReq.setHeader('cookie', addCookie(anonymousUserIdCookieName, newCookieValue, req.headers.cookie));
        req.headers[cookieHeaderTrackingName] = newCookieValue;
      }
    },
    onProxyRes: function(proxyRes, req, res) {
      const newCookieValue = req.headers[cookieHeaderTrackingName];
      if (newCookieValue) {
        proxyRes.headers['set-cookie'] = addCookie(
          anonymousUserIdCookieName,
          newCookieValue,
          proxyRes.headers['set-cookie'],
        );
      }
    },
    logLevel: 'debug',
  },
  '/api': {
    target: 'http://localhost:7777/api',
    secure: false,
    pathRewrite: {
      '^/api': '',
    },
    onProxyReq: function(proxyReq, req, res) {
      const anonymousUserId = getCookieValue(req.headers.cookie, anonymousUserIdCookieName);
      if (!anonymousUserId) {
        const newCookieValue = randomString();
        proxyReq.setHeader('cookie', addCookie(anonymousUserIdCookieName, newCookieValue, req.headers.cookie));
        req.headers[cookieHeaderTrackingName] = newCookieValue;
      }
    },
    onProxyRes: function(proxyRes, req, res) {
      const newCookieValue = req.headers[cookieHeaderTrackingName];
      if (newCookieValue) {
        proxyRes.headers['set-cookie'] = addCookie(
          anonymousUserIdCookieName,
          newCookieValue,
          proxyRes.headers['set-cookie'],
        );
      }
    },
    logLevel: 'debug',
  },
};

module.exports = PROXY_CONFIG;
