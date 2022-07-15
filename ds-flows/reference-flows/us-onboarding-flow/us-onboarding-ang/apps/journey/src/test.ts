// This file is required by karma.conf.js and loads recursively all the .spec and framework files

import 'zone.js/dist/zone-testing';

declare const require: any;

// First, initialize the Angular testing environment. -- TestBed is not used in this lib. If necessary uncomment.
// getTestBed().initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());
// Then we find all the tests.
const context = require.context('./', true, /\.spec\.ts$/);
// And load the modules.
context.keys().map(context);

// Add public_api for coverage
const publicApiContext = require.context('./', true, /public_api\.ts$/);
publicApiContext.keys().map(publicApiContext);
