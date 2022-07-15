import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { DsInteractionInitAngComponent } from './ds-interaction-init-ang.component';
import { EmptyStateModule, LoadingIndicatorModule } from '@backbase/ui-ang';

@NgModule({
  declarations: [DsInteractionInitAngComponent],
  imports: [
    CommonModule,
    LoadingIndicatorModule,
    EmptyStateModule,
    BackbaseCoreModule.withConfig({
      classMap: { DsInteractionInitAngComponent },
    }),
  ],
})
export class DsInteractionInitAngModule {}
