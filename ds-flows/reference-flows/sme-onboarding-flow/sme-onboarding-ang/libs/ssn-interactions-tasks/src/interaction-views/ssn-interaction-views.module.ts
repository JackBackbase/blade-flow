import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CoreAngModule } from '@backbase/cm-core-ang';
import { DsSsnJourneyAngModule } from '@backbase/ono-journey-collection-ang/journeys';
import { IconModule } from '@backbase/ui-ang';
import { SsnInteractionTasksDoneComponent } from './ssn-interaction-views-done.component';
import { SsnInteractionTasksFormComponent } from './ssn-interaction-views-form.component';

const viewModules = [DsSsnJourneyAngModule];

const viewComponents = [SsnInteractionTasksFormComponent, SsnInteractionTasksDoneComponent];

@NgModule({
  imports: [
    ...viewModules,
    CommonModule,
    IconModule,
    CoreAngModule.withConfig({
      viewMap: {
        'sme-onboarding-ssn': SsnInteractionTasksFormComponent,
        done: SsnInteractionTasksDoneComponent,
      },
    }),
  ],
  declarations: [...viewComponents],
  exports: [...viewComponents],
})
export class SsnInteractionViewsModule {}
