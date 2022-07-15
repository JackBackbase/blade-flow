import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ItemModel } from '@backbase/foundation-ang/core';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { Customer } from '@backbase/ac-common-ang';
import { pluck } from 'rxjs/operators';

const ACTION_NAME = 'send-registrar-details';

@Component({
  selector: 'bb-ac-header-widget',
  templateUrl: 'ac-header.component.html',
})
export class AcHeaderWidgetComponent implements OnInit {
  registrarDetails?: Partial<Customer>;
  readonly subtitle$: Observable<string> = this.model.property<string>('subtitle', '');
  readonly isLoading$ = new BehaviorSubject<boolean>(false);
  readonly hasHttpError$ = new BehaviorSubject<boolean>(false);

  constructor(private readonly flowInteractionService: FlowInteractionService, private readonly model: ItemModel) {}

  ngOnInit() {
    this.getRegistrar();
  }

  private async getRegistrar() {
    this.isLoading$.next(true);
    try {
      this.registrarDetails = await this.flowInteractionService
        .call({
          action: ACTION_NAME,
          body: {},
        })
        .pipe(pluck('body'))
        .toPromise();
    } catch {
      this.hasHttpError$.next(true);
    } finally {
      this.isLoading$.next(false);
    }
  }
}
