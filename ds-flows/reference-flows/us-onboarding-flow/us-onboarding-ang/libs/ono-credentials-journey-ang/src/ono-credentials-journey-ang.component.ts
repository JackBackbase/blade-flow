import { Component } from '@angular/core';
import { FormHelperFactory, FormHelper } from '@backbase/ono-common-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { CredentialsFormMetadata } from './credentials-form';
import { FormControl } from '@angular/forms';
import { FormlyValidator } from '@backbase/ds-shared-ang/ui';
import { take } from 'rxjs/operators';

@Component({
  selector: 'bb-ono-credentials-journey-ang',
  templateUrl: './ono-credentials-journey-ang.component.html',
  styleUrls: ['./ono-credentials-journey-ang.component.scss'],
  providers: [FormHelperFactory],
})
export class OnoCredentialsJourneyAngComponent {
  helper: FormHelper;
  mdnIsRejected = false;
  modalIsOpen = false;

  submitted = false;

  get passwordControl(): FormControl {
    return this.helper.form.controls['password'] as FormControl;
  }

  get has8Characters(): boolean {
    if (this.passwordControl.dirty) {
      return !!(this.passwordControl.value && !this.passwordControl.hasError('minlength'));
    }
    return false;
  }

  get includesNumber(): boolean {
    return this.passwordControl.dirty && !this.passwordControl.hasError(FormlyValidator.IncludesDigit);
  }

  get includesUppercase(): boolean {
    return this.passwordControl.dirty && !this.passwordControl.hasError(FormlyValidator.IncludesUppercase);
  }

  constructor(helperFactory: FormHelperFactory, private readonly _flowInteraction: FlowInteractionService) {
    this.helper = helperFactory.getHelper().setFieldsConfig(CredentialsFormMetadata);

    this._flowInteraction.cdo
      .get<string>('email')
      .pipe(take(1))
      .subscribe((email) => {
        if (email) {
          this.helper.setModel({ email });
        }
      });
  }

  async submitAction(): Promise<void> {
    this.submitted = true;
    const response = await this.helper.submit('setup-credentials');
    if (!response) {
      return;
    }

    if (response.actionErrors && response.actionErrors.length !== 0) {
      return;
    }
    this._flowInteraction.nav.next();
  }
}
