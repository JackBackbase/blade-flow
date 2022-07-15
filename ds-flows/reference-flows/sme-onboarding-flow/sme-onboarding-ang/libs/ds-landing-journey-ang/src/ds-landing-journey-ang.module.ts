import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { DsLandingJourneyAngComponent } from './ds-landing-journey-ang.component';
import { LoadButtonModule, TabModule, ModalModule, ButtonModule } from '@backbase/ui-ang';
import { LandingJourneyServiceConfigurationService } from './services/landing-journey-config.service';
import { provideRoutes, Route } from '@angular/router';

const defaultRoute: Route = {
  path: '',
  component: DsLandingJourneyAngComponent,
};

@NgModule({
  declarations: [DsLandingJourneyAngComponent],
  imports: [
    CommonModule,
    LoadButtonModule,
    TabModule,
    ModalModule,
    ButtonModule,
    BackbaseCoreModule.withConfig({
      classMap: { DsLandingJourneyAngComponent },
    }),
  ],
  providers: [LandingJourneyServiceConfigurationService],
  exports: [DsLandingJourneyAngComponent],
})
export class DsLandingJourneyAngModule {
  static forRoot(
    data: { [key: string]: unknown; route: Route } = { route: defaultRoute },
  ): ModuleWithProviders<DsLandingJourneyAngModule> {
    return {
      ngModule: DsLandingJourneyAngModule,
      providers: [provideRoutes([data.route])],
    };
  }
}
