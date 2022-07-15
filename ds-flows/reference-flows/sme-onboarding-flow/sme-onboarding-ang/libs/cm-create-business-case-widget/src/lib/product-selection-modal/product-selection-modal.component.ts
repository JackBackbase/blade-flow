import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { Observable, of, Subscription } from 'rxjs';
import { filter, switchMap, tap } from 'rxjs/operators';
import { CmCreateBusinessCaseWidgetService } from '../../cm-create-business-case-widget.service';
import { Product } from '../model/product.model';

@Component({
  selector: 'bb-cm-product-selection-modal',
  templateUrl: 'product-selection-modal.component.html',
  styleUrls: ['product-selection-modal.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductSelectionModalComponent implements OnDestroy, OnInit {
  submitted = false;
  products$?: Observable<Product[]>;
  selectedProduct?: Product;
  isLoading = false;

  @Input() openDialog = true;
  @Output() cancel = new EventEmitter();
  @Output() finish = new EventEmitter();
  @Output() startFromBeginning = new EventEmitter<string>();

  private readonly subs = new Subscription();

  readonly product = new FormControl('', [Validators.required]);
  readonly productSelectionForm = new FormGroup({
    product: this.product,
  });

  constructor(
    private readonly _flowInteractionService: FlowInteractionService,
    private readonly _widgetService: CmCreateBusinessCaseWidgetService,
  ) {}

  ngOnInit() {
    this.products$ = this._widgetService.getProductList();
    this.watchForUserSelection();
  }

  onCloseDialog() {
    this.openDialog = false;
    this.cancel.emit();
    this.startFromBeginning.emit('business-informations');
    this._widgetService.setBusinessInformationsFormValue(undefined);
  }

  allowToSubmit(): boolean {
    return !this.productSelectionForm.valid || this.isLoading;
  }

  async navigateTo(step: string) {
    await this._flowInteractionService.navigate(step).toPromise();
  }

  onConfirmDialog() {
    this.submitted = true;
    this.productSelectionForm.markAllAsTouched();

    if (this.allowToSubmit()) {
      return;
    }

    this.isLoading = true;
    const formValue = this.productSelectionForm.controls.product.value;

    this.subs.add(
      this._flowInteractionService
        .call({
          action: 'select-products',
          body: {
            payload: {
              selection: [
                {
                  productName: this.selectedProduct?.name,
                  referenceId: formValue,
                },
              ],
            },
          },
        })
        .subscribe((data) => {
          if (data && data.actionErrors && data.actionErrors.length) {
            data.actionErrors.forEach((error: any) => {
              if (error.context) {
                Object.keys(error.context).forEach((key: string) => {
                  if (this.productSelectionForm && this.productSelectionForm.controls[key]) {
                    this.productSelectionForm.controls[key].setErrors({ flow: error.context[key] });
                  }
                });
              }
            });

            return;
          }
          this.startFromBeginning.emit('business-informations');
          this._widgetService.setBusinessInformationsFormValue(undefined);
          this.finish.emit();
          this.isLoading = false;
        }),
    );
  }

  watchForUserSelection() {
    this.subs.add(
      (this.selectedProduct = this.productSelectionForm
        .get('product')
        ?.valueChanges.pipe(
          switchMap((productFromSelection: string) => {
            if (this.products$) {
              return this.products$.pipe(
                switchMap((wantedProduct: Product[]) => wantedProduct),
                filter((wantedProduct: Product) => wantedProduct.referenceId === productFromSelection),
                tap((wantedProduct) => {
                  this.selectedProduct = wantedProduct;
                }),
              );
            }

            return of();
          }),
        )
        .subscribe()),
    );
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }
}
