import { Directive, TemplateRef, Input } from '@angular/core';
import { SuspenseState } from './suspense.directive';
import { SuspenseBaseDirective } from './suspense-base-directive';

@Directive({
  selector: '[bbSuspenseSuccess]',
})
export class SuspenseSuccessDirective extends SuspenseBaseDirective {
  @Input('bbSuspenseSuccess') customTemplate?: TemplateRef<any>;

  needsToBeRendered(state: SuspenseState) {
    return !state.loading && !state.error;
  }
}
