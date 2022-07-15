import { FormlyFieldConfig } from '@ngx-formly/core';
import { InputType, FormlyValidator } from '@backbase/ds-shared-ang/ui';

export const CredentialsFormMetadata: FormlyFieldConfig[] = [
  {
    type: InputType.Email,
    key: 'email',
    templateOptions: { label: 'Email address', required: true },
    expressionProperties: { 'templateOptions.readonly': (model) => !!model.email },
    validators: {},
  },
  {
    type: InputType.Password,
    key: 'password',
    templateOptions: { label: 'Create a password', showVisibilityControl: true, minLength: 8, required: true },
    validators: { validation: [FormlyValidator.IncludesDigit, FormlyValidator.IncludesUppercase] },
  },
];
