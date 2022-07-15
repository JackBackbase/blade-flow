import { ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { LoadingIndicatorModule } from '@backbase/ui-ang';
import { DsCallActionAngComponent } from './ds-call-action-ang.component';
import { provideRoutes, Route } from '@angular/router';

const defaultRoute: Route = {
  path: '',
  component: DsCallActionAngComponent,
};

@NgModule({
  declarations: [DsCallActionAngComponent],
  imports: [
    CommonModule,
    BackbaseCoreModule.withConfig({
      classMap: { DsCallActionAngComponent },
    }),
    LoadingIndicatorModule,
  ],
})
export class DsCallActionAngModule {
  static forRoot(
    data: { [key: string]: unknown; route: Route } = { route: defaultRoute },
  ): ModuleWithProviders<DsCallActionAngModule> {
    return {
      ngModule: DsCallActionAngModule,
      providers: [provideRoutes([data.route])],
    };
  }
}
