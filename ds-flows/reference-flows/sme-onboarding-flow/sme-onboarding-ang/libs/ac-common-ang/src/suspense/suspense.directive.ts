import { Directive, Input, OnDestroy } from '@angular/core';
import { Observable, Subscription, ReplaySubject } from 'rxjs';

export interface SuspenseState {
  loading?: boolean;
  error?: any;
  data?: any;
}

@Directive({
  selector: '[bbSuspense]',
})
export class SuspenseDirective implements OnDestroy {
  @Input()
  set bbSuspense(obj: Observable<any>) {
    this.unsubscribe();

    if (!obj) {
      throw new Error('[SuspenseDirective] Input bbSuspense should be Observable');
    }

    if (obj !== this.objValue) {
      this.subscribe(obj);
      this.objValue = obj;
    }
  }

  state$ = new ReplaySubject<SuspenseState>(1);

  private subscription?: Subscription;
  private objValue?: Observable<any>;

  ngOnDestroy() {
    this.unsubscribe();
  }

  private subscribe(obj: Observable<any>) {
    this.state$.next({ loading: true });

    this.subscription = obj.subscribe({
      next: (data) => this.state$.next({ data }),
      error: (error) => this.state$.next({ error }),
    });
  }

  private unsubscribe() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
