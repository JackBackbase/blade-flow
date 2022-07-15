import {
  Directive,
  OnInit,
  OnDestroy,
  TemplateRef,
  ViewContainerRef,
  ChangeDetectorRef,
  EmbeddedViewRef,
} from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil, distinctUntilChanged } from 'rxjs/operators';
import { SuspenseDirective, SuspenseState } from './suspense.directive';

@Directive({
  selector: '[bbSuspenseBaseDirective]',
})
export class SuspenseBaseDirective implements OnInit, OnDestroy {
  protected onDestroy$ = new Subject();
  protected viewRef?: EmbeddedViewRef<any>;
  protected customTemplate?: TemplateRef<any>;

  constructor(
    protected readonly suspense: SuspenseDirective,
    protected readonly templateRef: TemplateRef<any>,
    protected readonly viewContainerRef: ViewContainerRef,
    protected readonly changeDetection: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.suspense.state$
      .pipe(distinctUntilChanged(), takeUntil(this.onDestroy$))
      .subscribe((state) => this.render(state));
  }

  ngOnDestroy() {
    this.onDestroy$.next();
    this.onDestroy$.complete();
  }

  needsToBeRendered(state: SuspenseState): boolean {
    throw new Error('needsToBeRendered method should be owerwriten');
  }

  getTemplateContext(state: SuspenseState): { $implicit: any; [key: string]: any } {
    return { $implicit: state.data };
  }

  protected render(state: SuspenseState) {
    this.viewContainerRef.clear();
    this.viewRef = undefined;

    if (this.needsToBeRendered(state)) {
      this.viewRef = this.getViewRef(this.getTemplateContext(state));
      this.changeDetection.markForCheck();
    }
  }

  protected getViewRef(ctx: any) {
    return this.viewRef || this.viewContainerRef.createEmbeddedView(this.customTemplate || this.templateRef, ctx);
  }
}
