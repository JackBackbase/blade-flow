import { Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { TaskDetailsView } from '@backbase/case-management-ui-ang/core';
import { CLOSE_MODAL_PUBSUB_EVENT, OPEN_MODAL_PUBSUB_EVENT } from '@backbase/cm-modal-overlay-container';
import { PUBSUB, Pubsub } from '@backbase/foundation-ang/web-sdk';
import { idtImage } from './ssn-interaction-tasks.image';

@Component({
  selector: 'bb-ssn-interaction-tasks',
  templateUrl: './ssn-interaction-tasks.component.html',
  styleUrls: ['./ssn-interaction-tasks.component.scss'],
})
export class SsnInteractionTasksComponent extends TaskDetailsView {
  iconsConfig = {
    size: 'sm',
    icons: [
      {
        name: 'group',
        text: 'With client',
      },
      {
        name: 'schedule',
        text: '5 min',
      },
      {
        name: 'no-sim',
        text: 'No documents',
      },
    ],
  };
  idtImage = idtImage.url;

  constructor(@Inject(PUBSUB) private readonly eventBus: Pubsub, private readonly router: Router) {
    super();
  }

  loadTaskJob(): void {
    this.eventBus.publish(OPEN_MODAL_PUBSUB_EVENT, {});
    this.eventBus.subscribe(CLOSE_MODAL_PUBSUB_EVENT, () => {
      const url = this.router.url;
      const baseUrl = url.slice(0, url.indexOf(';taskInstanceId'));
      this.router.navigateByUrl(`${baseUrl}`);
    });
  }
}
