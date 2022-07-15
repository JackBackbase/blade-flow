import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { CaseDataService } from '@backbase/cm-case-common-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { CmCreateBusinessCaseWidgetService } from '../../cm-create-business-case-widget.service';
import { MatchDataViewService } from '../../helpers/matchDataViewService';
import { businessInformations } from '../model/business-informations.model';

@Component({
  selector: 'bb-cm-exsisting-case-modal',
  templateUrl: 'exsisting-case-modal.component.html',
  styleUrls: ['exsisting-case-modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ExsistingCaseModalComponent implements OnDestroy, OnInit {
  businessInformation?: businessInformations;
  caseStatus? = '';
  @Input() instanceKey = '';
  @Input() openDialog = true;
  @Output() cancel = new EventEmitter();
  @Output() goToExsistingCase = new EventEmitter();
  @Output() startFromBeginning = new EventEmitter<string>();

  private readonly subs = new Subscription();

  constructor(
    private readonly _flowInteractionService: FlowInteractionService,
    private readonly _widgetService: CmCreateBusinessCaseWidgetService,
    private readonly _caseDataService: CaseDataService,
    readonly _matchDataViewService: MatchDataViewService,
    private readonly cd: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.businessInformation = this._widgetService.getBusinessInformationsFormValue();
    this.getCaseStatus();
  }

  getCaseStatus() {
    this._caseDataService.loadCaseInstance(this.instanceKey);
    this.subs.add(
      this._caseDataService.caseInstance$
        .pipe(filter((caseInstance) => !!caseInstance && !!caseInstance.primaryStatus))
        .subscribe((caseInstance) => {
          this.caseStatus = caseInstance?.primaryStatus?.toUpperCase();
          this.cd.markForCheck();
        }),
    );
  }

  onCloseDialog() {
    this.openDialog = false;
    this.cancel.emit();
    this.startFromBeginning.emit('business-informations');
    this._widgetService.setBusinessInformationsFormValue(undefined);
  }

  async navigateTo(step: string) {
    await this._flowInteractionService.navigate(step).toPromise();
  }

  onGoToCase() {
    this.goToExsistingCase.emit();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
