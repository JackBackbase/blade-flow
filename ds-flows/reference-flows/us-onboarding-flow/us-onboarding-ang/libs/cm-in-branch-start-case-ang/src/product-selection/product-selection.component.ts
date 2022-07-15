import { ChangeDetectorRef, Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';

import { InputType } from '@backbase/ds-shared-ang/ui';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { FormHelperFactory, FormHelper } from '@backbase/ono-journey-collection-ang/common';
import { FormlyFieldConfig } from '@ngx-formly/core';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'bb-product-selection',
  templateUrl: './product-selection.component.html',
  styleUrls: ['./product-selection.component.scss'],
  providers: [FormHelperFactory],
})
export class ProductSelectionComponent implements OnInit, OnDestroy {
  helper: FormHelper;
  loading$ = new BehaviorSubject(false);
  submitted = false;

  @Output() next = new EventEmitter();
  @Output() cancelled = new EventEmitter();

  constructor(
    private flowInteractionService: FlowInteractionService,
    helperFactory: FormHelperFactory,
    private changeDetector: ChangeDetectorRef,
  ) {
    this.helper = helperFactory.getHelper().setFieldsConfig([]);
  }

  async ngOnInit(): Promise<void> {
    const response = await this.flowInteractionService.call({ action: 'get-product-list', body: {} }).toPromise();

    if (response?.actionErrors?.length) return;

    const options = response?.body?.map((product: { referenceId: string; name: string }) => {
      return {
        value: product.referenceId,
        label: product.name,
      };
    });

    const fields: FormlyFieldConfig[] = [
      {
        type: InputType.RadioGroup,
        key: 'product',
        templateOptions: {
          required: true,
          options: options,
        },
      },
    ];

    this.helper
      .setFieldsConfig(fields)
      .setModel({
        products: options[0]?.value,
      })
      .setPayloadMapper(() => {
        return {
          selection: [{ referenceId: this.helper.value.product }],
        };
      });
    this.changeDetector.detectChanges();
  }

  async submit(): Promise<void> {
    this.submitted = true;
    if (!this.helper.value) return;
    if (this.loading$.value) return;

    this.loading$.next(true);

    const response = await this.helper.submit('select-products');

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
        this.changeDetector.detectChanges();
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
