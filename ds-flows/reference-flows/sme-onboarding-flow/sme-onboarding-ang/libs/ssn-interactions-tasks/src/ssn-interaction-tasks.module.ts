import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CoreAngModule } from '@backbase/case-management-ui-ang/core';
import { IdtTaskViewModule } from '@backbase/ds-shared-ang/ui';
import { SsnInteractionViewsModule } from './interaction-views/ssn-interaction-views.module';
import { SsnInteractionTasksComponent } from './ssn-interaction-tasks.component';

@NgModule({
  declarations: [SsnInteractionTasksComponent],
  imports: [
    CommonModule,
    IdtTaskViewModule,
    SsnInteractionViewsModule,
    CoreAngModule.withConfig({
      viewMap: {
        'ssn-idt': SsnInteractionTasksComponent,
      },
    }),
  ],
})
export class SsnInteractionTasksModule {}
