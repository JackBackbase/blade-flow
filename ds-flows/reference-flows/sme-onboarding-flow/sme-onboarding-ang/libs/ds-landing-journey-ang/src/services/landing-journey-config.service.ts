import { Inject, Injectable, InjectionToken, Optional } from '@angular/core';

export interface LandingJourneyConfiguration {
  linkNavigateTo: string;
  heading: string;
}

export const LANDING_JOURNEY_CONFIGURATION = new InjectionToken<LandingJourneyConfiguration>(
  'LandingJourneyServiceConfiguration injection token',
);

const configDefault: LandingJourneyConfiguration = {
  linkNavigateTo: '',
  heading: '',
};

@Injectable()
export class LandingJourneyServiceConfigurationService {
  private config: LandingJourneyConfiguration;

  constructor(@Optional() @Inject(LANDING_JOURNEY_CONFIGURATION) config: LandingJourneyConfiguration) {
    this.config = { ...configDefault, ...config };
  }

  get defaults(): Partial<LandingJourneyConfiguration> {
    return configDefault;
  }

  get journeyConfig(): LandingJourneyConfiguration {
    return this.config;
  }
}
