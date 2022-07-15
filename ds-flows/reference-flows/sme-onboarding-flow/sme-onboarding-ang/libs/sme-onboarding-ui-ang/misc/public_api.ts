import { NgModule } from '@angular/core';
import { DsInteractionInitAngModule as DsInteractionInitAngModuleWrapped } from '@backbase/ds-interaction-init-ang';

@NgModule({
  imports: [DsInteractionInitAngModuleWrapped],
  exports: [DsInteractionInitAngModuleWrapped],
})
export class DsInteractionInitAngModule {}
export { DsInteractionInitAngComponent } from '@backbase/ds-interaction-init-ang';
