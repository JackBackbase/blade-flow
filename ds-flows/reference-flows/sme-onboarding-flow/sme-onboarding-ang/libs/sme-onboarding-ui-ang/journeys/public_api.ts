import { NgModule } from '@angular/core';
import { DsAddressJourneyAngModule as DsAddressJourneyAngModuleWrapped } from '@backbase/ds-address-journey-ang';
import { DsLandingJourneyAngModule as DsLandingJourneyAngModuleWrapped } from '@backbase/ds-landing-journey-ang';
import { DsAnchorJourneyAngModule as DsAnchorJourneyAngModuleWrapped } from '@backbase/ds-anchor-journey-ang';
import { DsCallActionAngModule as DsCallActionAngModuleWrapped } from '@backbase/ds-call-action-ang';
import { CmCreateBusinessCaseWidgetModule as CmCreateBusinessCaseWidgetModuleWrapped } from '@backbase/cm-create-business-case-widget';

@NgModule({
  imports: [DsAddressJourneyAngModuleWrapped],
  exports: [DsAddressJourneyAngModuleWrapped],
})
export class DsAddressJourneyAngModule {}
export { DsAddressJourneyAngComponent } from '@backbase/ds-address-journey-ang';

@NgModule({
  imports: [DsLandingJourneyAngModuleWrapped],
  exports: [DsLandingJourneyAngModuleWrapped],
})
export class DsLandingJourneyAngModule {}
export { DsLandingJourneyAngComponent } from '@backbase/ds-landing-journey-ang';

@NgModule({
  imports: [DsAnchorJourneyAngModuleWrapped],
  exports: [DsAnchorJourneyAngModuleWrapped],
})
export class DsAnchorJourneyAngModule {}
export { DsAnchorJourneyAngComponent } from '@backbase/ds-anchor-journey-ang';

@NgModule({
  imports: [DsCallActionAngModuleWrapped],
  exports: [DsCallActionAngModuleWrapped],
})
export class DsCallActionAngModule {}
export { DsCallActionAngComponent } from '@backbase/ds-call-action-ang';

@NgModule({
  imports: [CmCreateBusinessCaseWidgetModuleWrapped],
  exports: [CmCreateBusinessCaseWidgetModuleWrapped],
})
export class CmCreateBusinessCaseWidgetModule {}
export { CmCreateBusinessCaseWidgetComponent } from '@backbase/cm-create-business-case-widget';
