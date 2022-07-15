import { FormlyFieldConfig } from '@ngx-formly/core';
import { InputType } from '@backbase/ds-shared-ang/ui';

function getDateInThePast(yearsOffset: number) {
  const today = new Date();
  return new Date(today.getFullYear() - yearsOffset, today.getMonth(), today.getDate(), 23, 59, 59).toISOString();
}

export const PersonalInformationFormMetadata: FormlyFieldConfig[] = [
  {
    fieldGroupClassName: 'd-flex',
    fieldGroup: [
      {
        type: InputType.Text,
        key: 'firstName',
        className: 'w-100 mr-4',
        templateOptions: {
          required: true,
          label: 'First name',
          maxLength: 50,
        },
      },
      {
        type: InputType.Text,
        key: 'lastName',
        className: 'w-100',
        templateOptions: {
          label: 'Last name',
          required: true,
          maxLength: 50,
        },
      },
    ],
  },
  {
    type: InputType.DividedDate,
    key: 'dateOfBirth',
    className: 'd-md-block w-50',
    templateOptions: {
      label: 'Date of birth',
      overrideDateFormat: 'MM/dd/yyyy',
      minDate: getDateInThePast(100),
      maxDate: getDateInThePast(18),
      required: true,
    },
    validators: [],
  },
  {
    type: InputType.Email,
    key: 'email',
    className: 'd-md-block w-75',
    templateOptions: {
      label: 'Email address',
      required: true,
    },
    validators: [],
  },
  {
    type: InputType.Text,
    key: 'phoneNumber',
    className: 'd-md-block w-75',
    templateOptions: {
      label: 'Phone number',
      required: true,
      maxLength: 12,
      placeholder: '+1',
    },
  },
];
