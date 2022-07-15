import { Component, Input, OnDestroy } from '@angular/core';
import { ContentService } from '@backbase/content-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'bb-sme-onboarding-walkthrough-pages-ang',
  templateUrl: 'ds-sme-onboarding-walkthrough-pages-ang.component.html',
  providers: [ContentService],
})
export class SmeOnboardingWalkthroughPagesComponent implements OnDestroy {
  tncCompleted = false;
  private subs = new Subscription();

  @Input() privacyLink?: string;
  @Input() privacyLinkText?: string;
  @Input() tncLink?: string;
  @Input() tncLinkText?: string;

  constructor(private readonly _flowInteraction: FlowInteractionService) {}

  onWalkthroughModalComplete(): void {
    this.subs.add(
      this._flowInteraction
        .call({
          action: 'sme-onboarding-init',
          body: {
            payload: { accepted: true },
          },
        })
        .subscribe({
          next: (response) => {
            if (response && response.actionErrors && response.actionErrors.length !== 0) {
              return;
            }
            this._flowInteraction.nav.next();
          },
        }),
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
