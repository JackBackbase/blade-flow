# SME Onboarding (SMEO)

## Prerequisites

Install the dependencies by running the following command

```bash
npm install
```

## Development mode

To run the project in development mode first run `setup` command

```bash
npm run setup
```

which should install web sdk specified at `pom.xml`

then run `npm pre:start` command to build the application simplified models

```bash
npm pre:start
```

and finally, start the applications. There are several options.

```bash
npm run start
```

will start both the employee and the prospect app (ports 4200 and 4201).

Other option is to start both of them separately,

To start the employee app, execute the command below. The app will be initialized in your localhost, port 4200

```bash
start:employee-app
```

To start the prospect app, execute the command below. The app will be initialized in your localhost, port 4201

```bash
start:prospect-app
```

For more information visit `https://community.backbase.com`

## Create provision packages to be provisioned

Run the following command to create provision packages whenever you want to import you app in CXS.

```bash
npm run package:apps
```

This command will generate two zip files, in the `dist/zoz/<YOUR_APP_NAME>`

```bash
- cx.zip
- portal.zip
```

There are several ways to import this two packages into CXS, but the easiest is to go to CXP Manager, and then import both zips there, just by dropping them into the import box.

You must follow this order of imports:

- `cx.zip` goes first (It contains the base code packaged).
- wait for the success message.
- `portal.zip` this is the experience (portal) itself.

*NOTE: Every time you make a change in your local environment, and you plan to import your changes into CXS to update your existing experiences, you need to create the provisioning packages again.*
