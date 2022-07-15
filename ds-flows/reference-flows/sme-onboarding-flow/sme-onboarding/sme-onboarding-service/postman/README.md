Testing scenarios for `newman`:

1. SoleProp onboarding without business license:

   `newman run sme-onboarding-online-flow-sole-prop.json -e sme-onboarding.postman_environment.json --delay-request 50 --verbose --folder 'Gateway' --folder 'Sole proprietorship' --folder 'Company Lookup' --folder 'IDV and Personal Details' --folder 'Get Case'`
