import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { PAGE_CONFIG, PageConfig } from '@backbase/foundation-ang/web-sdk';

@Component({
  selector: 'bb-cm-in-branch-start-case-ang',
  templateUrl: './cm-in-branch-start-case-ang.component.html',
  styleUrls: ['./cm-in-branch-start-case-ang.component.scss'],
})
export class CmInBranchStartCaseAngComponent {
  currentStep = 'select-products';
  isModalOpen = false;

  @Input() apiVersion = 'v2';
  @Input() servicePath = 'us-onboarding/client-api/interaction';
  @Input() deploymentPath = 'interactions';
  @Input() interactionName = 'in-branch-onboarding';
  @Input() customClassName = '';

  @Output() caseInstanceKey = new EventEmitter<string>();

  constructor(
    private flowInteractionService: FlowInteractionService,
    @Inject(PAGE_CONFIG) private readonly pageConfig: PageConfig,
  ) {}

  openModal(): void {
    this.flowInteractionService.init(
      {
        apiVersion: this.apiVersion,
        servicePath: this.servicePath,
        deploymentPath: this.deploymentPath,
        apiRoot: this.pageConfig.apiRoot,
      },
      this.interactionName,
    );

    this.isModalOpen = true;
  }

  goToStep(step: string): void {
    if (!step) return;
    this.currentStep = step;
  }

  async finish(): Promise<void> {
    const response = await this.flowInteractionService.call({ action: 'submit-in-branch', body: {} }).toPromise();
    if (response && response.actionErrors && response.actionErrors.length) {
      // TODO: error handling -- MT
      return;
    }
    const caseId = response.body.caseKey;
    this.caseInstanceKey.emit(caseId);
    this.closeModal();
  }

  closeModal(): void {
    this.isModalOpen = false;
    this.currentStep = 'select-products';
  }
}
