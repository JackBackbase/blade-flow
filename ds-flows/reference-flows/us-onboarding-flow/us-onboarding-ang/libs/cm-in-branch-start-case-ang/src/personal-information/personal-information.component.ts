import { DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';

import { FormHelper, FormHelperFactory } from '@backbase/ono-journey-collection-ang/common';
import { BehaviorSubject } from 'rxjs';

import { PersonalInformationFormMetadata } from './personal-information-form';

@Component({
  selector: 'bb-personal-information',
  templateUrl: './personal-information.component.html',
  styleUrls: ['./personal-information.component.scss'],
  providers: [DatePipe, FormHelperFactory],
})
export class PersonalInformationComponent implements OnInit, OnDestroy {
  fields = PersonalInformationFormMetadata;
  loading$ = new BehaviorSubject(false);
  submitted = false;
  helper: FormHelper;

  @Output() next = new EventEmitter();
  @Output() cancelled = new EventEmitter();

  constructor(
    helperFactory: FormHelperFactory,
    private readonly datePipe: DatePipe,
    private readonly _changeDetection: ChangeDetectorRef,
  ) {
    this.helper = helperFactory.getHelper().setFieldsConfig([]);
  }

  ngOnInit(): void {
    this.helper
      .setFieldsConfig(this.fields)
      .setModel({
        dateOfBirth: '',
      })
      .setPayloadMapper(() => {
        return {
          ...this.helper.form.value,
          dateOfBirth: this.datePipe.transform(this.helper.form.value.dateOfBirth, 'yyyy-MM-dd'),
        };
      });
    this.helper.form.markAsUntouched();
    this.helper.form.updateValueAndValidity();
  }

  async submit(): Promise<void> {
    this.submitted = true;
    this.helper.form.markAllAsTouched();

    if (this.helper.invalid) return;

    if (this.loading$.value) return;
    this.loading$.next(true);

    const response = await this.helper.submit('submit-personal-info');

    if (response?.actionErrors?.length) {
      this.handleResponseErrors(response.actionErrors);
    } else {
      this.next.emit(response?.step?.name);
    }
  }

  handleResponseErrors(errors: any) {
    this.loading$.next(false);
    if (!Array.isArray(errors)) return;
    errors.forEach((error: any) => {
      Object.keys(error.context).forEach((key: string) => {
        if (!this.helper.form.get(key)) {
          this.helper.form.setErrors({ responseError: error.context[key] });
          this.submitted = false;
        } else {
          const control = this.helper.form.get(key);
          if (control) control.setErrors({ responseError: error.context[key] });
        }
        this._changeDetection.detectChanges();
      });
    });
  }

  cancel(): void {
    this.cancelled.emit();
  }

  ngOnDestroy(): void {
    this.loading$.next(false);
  }
}
