import { Injectable } from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { NotificationService } from '@backbase/ui-ang';
import { BehaviorSubject, Observable } from 'rxjs';
import { filter, map, mergeMap } from 'rxjs/operators';
import { Country, State } from '../src/lib/model/country.model';
import { businessInformations } from './lib/model/business-informations.model';
import { NotificationMessage, NotificationModifier } from './lib/model/notification.model';
import { Product } from './lib/model/product.model';

@Injectable({
  providedIn: 'root',
})
export class CmCreateBusinessCaseWidgetService {
  private readonly businessInformationsFormValueSource = new BehaviorSubject<any>(undefined);
  readonly businessInformationsData$ = this.businessInformationsFormValueSource.asObservable();

  constructor(
    private readonly _flowInteractionService: FlowInteractionService,
    private readonly _notificationService: NotificationService,
  ) {}

  getBusinessInformationsFormValue(): businessInformations | undefined {
    return this.businessInformationsFormValueSource.getValue();
  }

  setBusinessInformationsFormValue(formData: businessInformations | undefined): void {
    this.businessInformationsFormValueSource.next(formData);
  }

  getUsaStates(): Observable<State[]> {
    return this._flowInteractionService.getCollection('countries').pipe(
      map((response) => response.items as Country[]),
      mergeMap((country) => country),
      filter((country) => country.isoCode === 'US'),
      map((country) => country.states),
    );
  }

  pushNotification(message: NotificationMessage = '', modifier: NotificationModifier): void {
    this._notificationService.showNotification({
      message,
      modifier,
      dismissible: true,
      ttl: 5000,
    });
  }

  getProductList(): Observable<Product[]> {
    return this._flowInteractionService
      .call({
        action: 'get-product-list',
        body: {},
      })
      .pipe(
        filter((data) => !!data && !!data.body),
        map((data) => data.body),
      );
  }
}
