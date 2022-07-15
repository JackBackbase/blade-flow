import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Inject,
  Input,
  OnDestroy,
  OnInit,
  Output,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { PageConfig, PAGE_CONFIG } from '@backbase/foundation-ang/web-sdk';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { CmCreateBusinessCaseWidgetService } from './cm-create-business-case-widget.service';
import { businessInformations } from './lib/model/business-informations.model';
import { State } from './lib/model/country.model';
@Component({
  selector: 'bb-cm-create-business-case-widget',
  templateUrl: './cm-create-business-case-widget.component.html',
  styles: [],
})
export class CmCreateBusinessCaseWidgetComponent implements OnInit, OnDestroy {
  @Input() apiVersion = 'v2';
  @Input() servicePath = 'sme-onboarding/client-api/interaction';
  @Input() deploymentPath = 'interactions';
  @Input() interactionName = 'in-branch-onboarding-start';
  /**
   * Emit key of a selected case instance (WA3 navigation event)
   */
  @Output() caseInstanceKey = new EventEmitter<string>();
  /**
   * Notification success template
   */
  @ViewChild('notificationSuccessTpl', { static: true })
  notificationSuccessTplRef: TemplateRef<any> | undefined;

  private readonly subs = new Subscription();

  caseKey = '';
  caseStatus = '';
  businessInformation?: businessInformations;
  usaStates$?: Observable<State[]>;
  currentStep = 'business-informations';

  private isCreateNewCaseModalOpen = new BehaviorSubject<boolean>(false);
  isCreateNewCaseModalOpen$ = this.isCreateNewCaseModalOpen.asObservable();

  constructor(
    private readonly _flowInteractionService: FlowInteractionService,
    private readonly _widgetService: CmCreateBusinessCaseWidgetService,
    @Inject(PAGE_CONFIG) private readonly pageConfig: PageConfig,
    private readonly cd: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.usaStates$ = this._widgetService.getUsaStates();
  }

  createNewCase() {
    this._flowInteractionService.init(
      {
        apiVersion: this.apiVersion,
        servicePath: this.servicePath,
        deploymentPath: this.deploymentPath,
        apiRoot: this.pageConfig.apiRoot,
      },
      this.interactionName,
    );

    this.openCreateNewCaseModal(true);
  }

  openCreateNewCaseModal(value: boolean) {
    this.isCreateNewCaseModalOpen.next(value);
  }

  checkIsCaseExsist() {
    this.subs.add(
      this._flowInteractionService
        .call({
          action: 'sme-onboarding-check-case-exist',
          body: {
            payload: {},
          },
        })
        .subscribe((data) => {
          if (data.body.caseExist) {
            this.goToStep('check-exsisting-case');
            this.cd.markForCheck();
            this.getCaseKey(data.body.caseKey);

            return;
          }
          this.goToStep('product-selection');
          this.cd.markForCheck();
        }),
    );
  }

  finish() {
    this.openCreateNewCaseModal(false);
    this._widgetService.pushNotification(this.notificationSuccessTplRef, 'success');
  }

  getCaseKey(caseKey: string) {
    this.caseKey = caseKey;
  }

  goToCreatedCase(caseKey: string) {
    this.caseInstanceKey.emit(caseKey);
  }

  goToStep(step: string) {
    if (!step) return;
    this.currentStep = step;
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
