import { FormatWidth, getLocaleDateFormat } from '@angular/common';
import { InputType } from '@backbase/ds-shared-ang/ui';
import { OnoAnchorJourneyConfig } from '@backbase/ono-anchor-journey-ang';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { environment } from '../../environments/environment';

function getDateInThePast(yearsOffset: number): string {
  const today = new Date();

  return new Date(today.getFullYear() - yearsOffset, today.getMonth(), today.getDate(), 23, 59, 59).toISOString();
}

export const ANCHOR_FORM_METADATA: FormlyFieldConfig[] = [
  {
    type: InputType.Text,
    key: 'firstName',
    id: 'ono-anchor-journey-ang--first-name',
    className: 'd-md-block w-50',
    templateOptions: {
      label: $localize`:@@bb.ono-anchor-journey-ang.firstName:First name`,
      required: true,
      maxLength: 50,
    },
    validation: {
      messages: {
        required: $localize`:@@bb.ono-anchor-journey-ang.firstNameRequired:First name is required`,
      },
    },
  },
  {
    type: InputType.Text,
    key: 'lastName',
    className: 'd-md-block w-50',
    id: 'ono-anchor-journey-ang--last-name',
    templateOptions: {
      label: $localize`:@@bb.ono-anchor-journey-ang.lastName:Last name`,
      required: true,
      maxLength: 50,
    },
    validation: {
      messages: {
        required: $localize`:@@bb.ono-anchor-journey-ang.lastNameRequired:Last name is required`,
      },
    },
  },
  {
    type: InputType.DividedDate,
    key: 'dateOfBirth',
    className: 'd-md-block w-50',
    id: 'ono-anchor-journey-ang--date-of-birth',
    templateOptions: {
      label: $localize`:@@bb.ono-anchor-journey-ang.dob:Date of birth`,
      required: true,
      overrideDateFormat: getLocaleDateFormat(environment.locale || 'en-US', FormatWidth.Short),
      minDate: getDateInThePast(100),
      maxDate: getDateInThePast(18),
    },
    validation: {
      messages: {
        required: $localize`:@@bb.ono-anchor-journey-ang.dobRequired:Date of birth is required`,
        dateGreaterThanMaxDate: $localize`:@@bb.ono-anchor-journey-ang.dobDateGreaterThanMaxDate:You must be between 18 and 99 to open an account`,
        dateLessThanMinDate: $localize`:@@bb.ono-anchor-journey-ang.dobDateLessThanMinDate:You must be between 18 and 99 to open an account`,
        invalidDateFormat: $localize`:@@bb.ono-anchor-journey-ang.dobInvalidDateFormat:Date of birth is not a valid date`,
      },
    },
  },
  {
    type: InputType.Email,
    key: 'email',
    className: 'd-md-block w-75',
    id: 'ono-anchor-journey-ang--email',
    templateOptions: {
      label: $localize`:@@bb.ono-anchor-journey-ang.email:Email address`,
      required: true,
    },
    validation: {
      messages: {
        required: $localize`:@@bb.ono-anchor-journey-ang.emailRequired:Email is required`,
        email: $localize`:@@bb.ono-anchor-journey-ang.emailInvalid:Email should be valid`,
      },
    },
  },
];

export const anchorDataConfig: OnoAnchorJourneyConfig = {
  form: ANCHOR_FORM_METADATA,
};
