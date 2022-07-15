import { NgModule } from '@angular/core';
import { SuspenseDirective } from './suspense.directive';
import { SuspenseSuccessDirective } from './suspense-success.directive';
import { SuspenseErrorDirective } from './suspense-error.directive';
import { SuspensePlaceholderDirective } from './suspense-placeholder.directive';
import { SuspenseBaseDirective } from './suspense-base-directive';

@NgModule({
  declarations: [
    SuspenseDirective,
    SuspenseSuccessDirective,
    SuspenseErrorDirective,
    SuspensePlaceholderDirective,
    SuspenseBaseDirective,
  ],
  exports: [SuspenseDirective, SuspenseSuccessDirective, SuspenseErrorDirective, SuspensePlaceholderDirective],
})
export class SuspenseModule {}
