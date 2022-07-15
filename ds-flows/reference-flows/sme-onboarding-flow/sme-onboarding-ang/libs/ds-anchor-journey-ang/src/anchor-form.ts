import { InputType } from '@backbase/ds-shared-ang/ui';
import { FormlyFieldConfig } from '@ngx-formly/core';

function getDateInThePast(yearsOffset: number) {
  const today = new Date();

  return new Date(today.getFullYear() - yearsOffset, today.getMonth(), today.getDate(), 23, 59, 59).toISOString();
}

export const AnchorFormMetadata: FormlyFieldConfig[] = [
  {
    type: InputType.Text,
    key: 'firstName',
    className: 'd-md-block w-50 mb-4',
    templateOptions: {
      label: 'First name',
      required: true,
      maxLength: 50,
    },
  },
  {
    type: InputType.Text,
    key: 'lastName',
    className: 'd-md-block w-50 mb-4',
    templateOptions: {
      label: 'Last name',
      required: true,
      maxLength: 50,
    },
  },
  {
    type: InputType.DividedDate,
    key: 'dateOfBirth',
    className: 'd-md-block w-50 mb-4',
    templateOptions: {
      label: 'Date of birth',
      required: true,
      overrideDateFormat: 'MM/dd/yyyy',
      minDate: getDateInThePast(100),
      maxDate: getDateInThePast(18),
    },
    validators: [],
  },
  {
    type: InputType.Phone,
    key: 'phoneNumber',
    className: 'd-md-block w-50',
    templateOptions: {
      label: 'Cellphone number',
      required: true,
      areaCode: '+1',
    },
    validators: [],
  },
  {
    type: InputType.Email,
    key: 'emailAddress',
    className: 'd-md-block w-75',
    templateOptions: {
      label: 'Email address',
      required: true,
    },
    validators: [],
  },
];
