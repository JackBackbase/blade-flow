import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AcActionService } from '@backbase/ac-common-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { BehaviorSubject, combineLatest, of, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

const CASE_KEY = 'caseKey';

@Component({
  selector: 'bb-ds-interaction-init-ang',
  templateUrl: 'ds-interaction-init-ang.component.html',
})
export class DsInteractionInitAngComponent implements OnInit, OnDestroy {
  readonly isLoading$ = new BehaviorSubject(false);
  readonly isHttpError$ = new BehaviorSubject(false);
  private readonly destroy$ = new Subject<void>();

  @Input() action = 'init';

  constructor(
    private readonly interactionService: FlowInteractionService,
    private readonly route: ActivatedRoute,
    private readonly acActionService: AcActionService,
  ) {}

  ngOnInit() {
    if (!this.acActionService.initialized) {
      combineLatest([of(this.action), this.route.queryParams])
        .pipe(takeUntil(this.destroy$))
        .subscribe(([action, params]) => {
          this.storeCaseKey(params.id);
          this.sendAction(action);
        });
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private async sendAction(action: string): Promise<void> {
    const id = sessionStorage.getItem(CASE_KEY);
    this.isLoading$.next(true);
    const body = id ? { payload: { id } } : {};
    try {
      await this.interactionService
        .call({
          action,
          body,
        })
        .toPromise();
      this.acActionService.initialized = true;
      this.interactionService.nav.next();
    } catch (e) {
      this.isHttpError$.next(true);
    } finally {
      this.isLoading$.next(false);
    }
  }

  private storeCaseKey(key: string) {
    if (key) {
      sessionStorage.setItem(CASE_KEY, key);
    }
  }
}
