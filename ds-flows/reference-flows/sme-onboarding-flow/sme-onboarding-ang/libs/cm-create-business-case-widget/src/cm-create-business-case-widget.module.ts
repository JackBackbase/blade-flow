import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { InputDividedDateUiModule } from '@backbase/ds-shared-ang/ui';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import {
  BadgeModule,
  ButtonModule,
  DropdownSingleSelectModule,
  HeaderModule,
  IconModule,
  InputRadioGroupModule,
  InputTextModule,
  InputValidationMessageModule,
  LoadButtonModule,
  ModalModule,
} from '@backbase/ui-ang';
import { CmCreateBusinessCaseWidgetComponent } from './cm-create-business-case-widget.component';
import { BusinessInformationsModalComponent } from './lib/business-informations-modal/business-informations-modal.component';
import { ExsistingCaseModalComponent } from './lib/exsisting-case-modal/exsisting-case-modal.component';
import { ProductSelectionModalComponent } from './lib/product-selection-modal/product-selection-modal.component';

const layoutModules = [
  ModalModule,
  ButtonModule,
  HeaderModule,
  IconModule,
  BadgeModule,
  ReactiveFormsModule,
  InputTextModule,
  InputRadioGroupModule,
  DropdownSingleSelectModule,
  InputValidationMessageModule,
  InputDividedDateUiModule,
  LoadButtonModule,
];

const userComponents = [
  CmCreateBusinessCaseWidgetComponent,
  BusinessInformationsModalComponent,
  ProductSelectionModalComponent,
  ExsistingCaseModalComponent,
];

@NgModule({
  declarations: [...userComponents],
  imports: [
    CommonModule,
    IconModule,
    BackbaseCoreModule.withConfig({
      classMap: { CmCreateBusinessCaseWidgetComponent },
    }),
    ...layoutModules,
  ],
})
export class CmCreateBusinessCaseWidgetModule {}
