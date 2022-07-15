import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BackbaseCoreModule } from '@backbase/foundation-ang/core';
import { AcHeaderWidgetComponent } from './ac-header-widget.component';
import { HeaderComponent } from './components/header.component';
import { AvatarModule, HeaderModule, LoadingIndicatorModule } from '@backbase/ui-ang';

@NgModule({
  declarations: [AcHeaderWidgetComponent, HeaderComponent],
  imports: [
    CommonModule,
    AvatarModule,
    HeaderModule,
    LoadingIndicatorModule,
    BackbaseCoreModule.withConfig({
      classMap: { AcHeaderWidgetComponent },
    }),
  ],
  exports: [AcHeaderWidgetComponent],
})
export class AcHeaderWidgetModule {}
