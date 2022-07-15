const chalk = require('chalk');
const { target, secure, changeOrigin, pathRewrite } = process.env.REMOTE ? JSON.parse(process.env.REMOTE) : {};
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
console.log(
  chalk.blue`proxy conf - process env REMOTE variables: \ntarget: %s \nsecure: %s \nchangeOrigin: %s \npathRewrite: %s`,
  target,
  secure,
  changeOrigin,
  pathRewrite,
  `\n`
);

const PROXY_CONFIG = {
  '/api/contentservices/api/contentstream/resourceRepository/contextRoot/static/items/bb-employee-ang/assets/jumioMock.html': {
    target: 'http://localhost:4200/assets/jumioMock.html',
    secure: false,
    pathRewrite: {
        '^/api/contentservices/api/contentstream/resourceRepository/contextRoot/static/items/bb-employee-ang/assets/jumioMock.html': '',
    },
    logLevel: 'debug',
  },
  '/jumioMock.html': {
      target: 'http://localhost:4200/assets/jumioMock.html',
      secure: false,
      pathRewrite: {
          '^/jumioMock.html': '',
      },
      logLevel: 'debug',
  },
  '/api': {
    target: 'http://localhost:7777/api',
    secure: false,
    pathRewrite: {
      '^/api': '',
    },
    onProxyReq: function (proxyReq, req, res) {
      const anonymousUserId = getCookieValue(req.headers.cookie, anonymousUserIdCookieName);
      if (!anonymousUserId) {
        const newCookieValue = randomString();
        proxyReq.setHeader('cookie', addCookie(anonymousUserIdCookieName, newCookieValue, req.headers.cookie));
        req.headers[cookieHeaderTrackingName] = newCookieValue;
      }
    },
    onProxyRes: function (proxyRes, req, res) {
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
  '/gateway/api': {
    target: target ? `${target}` : 'http://localhost:7777',
    secure: secure || false,
    changeOrigin: changeOrigin || false,
    pathRewrite: pathRewrite || {
      '^/gateway': '',
    },
    onProxyReq: function (proxyReq, req) {
      const anonymousUserId = getCookieValue(req.headers.cookie, anonymousUserIdCookieName);
      if (!anonymousUserId) {
        const newCookieValue = randomString();
        proxyReq.setHeader('cookie', addCookie(anonymousUserIdCookieName, newCookieValue, req.headers.cookie));
        req.headers[cookieHeaderTrackingName] = newCookieValue;
      }
    },
    onProxyRes: function (proxyRes, req) {
      const newCookieValue = req.headers[cookieHeaderTrackingName];
      if (newCookieValue) {
        proxyRes.headers['set-cookie'] = addCookie(
          anonymousUserIdCookieName,
          newCookieValue,
          proxyRes.headers['set-cookie']
        );
      }
    },
    logLevel: 'debug',
  }
};

module.exports = PROXY_CONFIG;