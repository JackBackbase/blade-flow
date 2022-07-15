import { TemplateRef, ViewContainerRef } from '@angular/core';
import { Subject } from 'rxjs';
import { SuspenseSuccessDirective } from './suspense-success.directive';
import { SuspenseDirective, SuspenseState } from './suspense.directive';

describe('SuspenseSuccessDirective', () => {
  let directive: SuspenseSuccessDirective;

  let suspenseMock: Partial<SuspenseDirective>;
  let templateRef: Partial<TemplateRef<any>>;
  let viewContainerRef: Partial<ViewContainerRef>;
  let changeDeteciton: { markForCheck: any };
  let stateMock: Subject<SuspenseState>;

  beforeEach(() => {
    stateMock = new Subject<SuspenseState>();
    suspenseMock = {
      state$: stateMock as any,
    };
    changeDeteciton = { markForCheck: jest.fn() };
    viewContainerRef = {};
    templateRef = {};
    directive = new SuspenseSuccessDirective(
      suspenseMock as any,
      templateRef as any,
      viewContainerRef as any,
      changeDeteciton as any,
    );
  });

  it('should not render on loading', () => {
    expect(directive.needsToBeRendered({ loading: true })).toEqual(false);
  });

  it('should not render on error', () => {
    expect(directive.needsToBeRendered({ error: true })).toEqual(false);
  });

  it('should render when loaded and no errors', () => {
    expect(directive.needsToBeRendered({})).toEqual(true);
  });
});
