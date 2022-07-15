import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  Output,
} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { Subscription } from 'rxjs';
import { CmCreateBusinessCaseWidgetService } from '../../cm-create-business-case-widget.service';
import { State } from '../model/country.model';

@Component({
  selector: 'bb-cm-business-informations-modal',
  templateUrl: 'business-informations-modal.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BusinessInformationsModalComponent implements OnDestroy {
  submitted = false;
  isLoading = false;

  @Input() openDialog = true;
  @Input() usaStates?: State[];
  @Output() cancel = new EventEmitter();
  @Output() caseKey = new EventEmitter<string>();
  @Output() checkIsCaseExsist = new EventEmitter();

  private readonly subs = new Subscription();

  readonly firstName = new FormControl('', [Validators.required]);
  readonly lastName = new FormControl('', [Validators.required]);
  readonly dateOfBirth = new FormControl('', [Validators.required]);
  readonly emailAddress = new FormControl('', [Validators.required]);
  readonly phoneNumber = new FormControl('', [Validators.required]);
  readonly businessInformationsForm = new FormGroup({
    firstName: this.firstName,
    lastName: this.lastName,
    dateOfBirth: this.dateOfBirth,
    emailAddress: this.emailAddress,
    phoneNumber: this.phoneNumber,
  });

  constructor(
    private readonly _flowInteractionService: FlowInteractionService,
    private readonly _widgetService: CmCreateBusinessCaseWidgetService,
    private readonly cd: ChangeDetectorRef,
  ) {}

  onCloseDialog() {
    this.openDialog = false;
    this.cancel.emit();
    this._widgetService.setBusinessInformationsFormValue(undefined);
  }

  allowToSubmit(): boolean {
    return !this.businessInformationsForm.valid && this.submitted;
  }

  onConfirmDialog() {
    this.submitted = true;
    this.businessInformationsForm.markAllAsTouched();
    if (!this.businessInformationsForm.valid || this.isLoading) {
      return;
    }

    this.isLoading = true;
    const formValue = this.businessInformationsForm.value;

    this.subs.add(
      this._flowInteractionService
        .call({
          action: 'sme-onboarding-anchor-data',
          body: {
            payload: formValue,
          },
        })
        .subscribe((data) => {
          if (data && data.actionErrors && data.actionErrors.length) {
            data.actionErrors.forEach((error: any) => {
              if (error.context) {
                Object.keys(error.context).forEach((key: string) => {
                  if (this.businessInformationsForm && this.businessInformationsForm.controls[key]) {
                    this.businessInformationsForm.controls[key].setErrors({ flow: error.context[key] });
                    this.isLoading = false;
                    this.cd.detectChanges();
                  }
                });
              }
            });

            return;
          }
          this.caseKey.emit(data.body.caseKey);
          this._widgetService.setBusinessInformationsFormValue(formValue);
          this.checkIsCaseExsist.emit();
          this.isLoading = false;
        }),
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
