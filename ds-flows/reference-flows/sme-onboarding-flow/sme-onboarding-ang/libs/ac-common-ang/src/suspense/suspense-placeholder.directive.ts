import { Directive, TemplateRef, Input, OnInit } from '@angular/core';
import { SuspenseState } from './suspense.directive';
import { takeUntil, switchMap, delay } from 'rxjs/operators';
import { of } from 'rxjs';
import { SuspenseBaseDirective } from './suspense-base-directive';

@Directive({
  selector: '[bbSuspensePlaceholder]',
})
export class SuspensePlaceholderDirective extends SuspenseBaseDirective implements OnInit {
  @Input()
  set bbSuspensePlaceholder(delayOrTemplate: number | TemplateRef<any>) {
    if (typeof delayOrTemplate === 'number') {
      this.delay = delayOrTemplate;
    } else {
      this.customTemplate = delayOrTemplate || null;
    }
  }

  @Input('bbSuspensePlaceholderDelay')
  set bbSuspensePlaceholderDelay(delayMs: number) {
    this.delay = delayMs;
  }

  @Input()
  set bbSuspensePlaceholderTemplate(template: TemplateRef<any>) {
    this.customTemplate = template;
  }

  private delay = 0;

  ngOnInit() {
    this.suspense.state$
      .pipe(
        takeUntil(this.onDestroy$),
        switchMap((state) => {
          return state.loading ? of(state).pipe(delay(this.delay)) : of(state);
        }),
      )
      .subscribe((state) => this.render(state));
  }

  needsToBeRendered(state: SuspenseState) {
    return !!state.loading;
  }
}
