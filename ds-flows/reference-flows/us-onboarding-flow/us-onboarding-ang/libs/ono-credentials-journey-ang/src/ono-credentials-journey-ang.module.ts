import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { OnoCredentialsJourneyAngComponent } from './ono-credentials-journey-ang.component';
import { LoadButtonModule, ModalModule, ButtonModule } from '@backbase/ui-ang';
import { FormlyUiModule } from '@backbase/ds-shared-ang/ui';

@NgModule({
  declarations: [OnoCredentialsJourneyAngComponent],
  imports: [
    CommonModule,
    LoadButtonModule,
    ModalModule,
    ButtonModule,
    FormlyUiModule,
    BackbaseCoreModule.withConfig({
      classMap: { OnoCredentialsJourneyAngComponent },
    }),
  ],
})
export class OnoCredentialsJourneyAngModule {}
