import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CmInBranchStartCaseAngComponent } from './cm-in-branch-start-case-ang.component';
import { ButtonModule, LoadButtonModule, ModalModule } from '@backbase/ui-ang';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { FormlyUiModule } from '@backbase/ds-shared-ang/ui';
import { PersonalInformationComponent } from './personal-information/personal-information.component';
import { ProductSelectionComponent } from './product-selection/product-selection.component';
import { OnoCommonAngModule } from '@backbase/ono-journey-collection-ang/common';

@NgModule({
  declarations: [PersonalInformationComponent, ProductSelectionComponent, CmInBranchStartCaseAngComponent],
  imports: [
    CommonModule,
    ButtonModule,
    LoadButtonModule,
    ModalModule,
    FormlyUiModule,
    OnoCommonAngModule,
    BackbaseCoreModule.withConfig({
      classMap: { CmInBranchStartCaseAngComponent },
    }),
  ],
})
export class CmInBranchStartCaseAngModule {}
