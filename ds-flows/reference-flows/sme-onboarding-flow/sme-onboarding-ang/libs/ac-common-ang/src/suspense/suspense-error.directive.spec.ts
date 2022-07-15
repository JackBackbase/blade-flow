import { TemplateRef, ViewContainerRef } from '@angular/core';
import { Subject } from 'rxjs';
import { SuspenseErrorDirective } from './suspense-error.directive';
import { SuspenseDirective, SuspenseState } from './suspense.directive';

describe('SuspenseErrorDirective', () => {
  let directive: SuspenseErrorDirective;

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
    directive = new SuspenseErrorDirective(
      suspenseMock as any,
      templateRef as any,
      viewContainerRef as any,
      changeDeteciton as any,
    );
  });

  it('should render when error is present', () => {
    expect(directive.needsToBeRendered({ error: 'foo' })).toBe(true);
  });

  it('should not render when there is no error', () => {
    expect(directive.needsToBeRendered({})).toBe(false);
  });

  it('should provide error as $implicit template context', () => {
    expect(directive.getTemplateContext({ error: 'foo' })).toEqual({ $implicit: 'foo' });
  });
});
