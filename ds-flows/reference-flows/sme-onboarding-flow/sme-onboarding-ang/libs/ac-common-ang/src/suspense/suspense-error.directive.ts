import { Directive, TemplateRef, Input } from '@angular/core';
import { SuspenseState } from './suspense.directive';
import { SuspenseBaseDirective } from './suspense-base-directive';

@Directive({
  selector: '[bbSuspenseError]',
})
export class SuspenseErrorDirective extends SuspenseBaseDirective {
  @Input('bbSuspenseError') customTemplate?: TemplateRef<any>;

  needsToBeRendered(state: SuspenseState) {
    return !!state.error;
  }

  getTemplateContext(state: SuspenseState) {
    return { $implicit: state.error };
  }
}
