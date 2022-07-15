import { TemplateRef, ViewContainerRef } from '@angular/core';
import { fakeAsync, tick } from '@angular/core/testing';
import { Subject } from 'rxjs';
import { SuspensePlaceholderDirective } from './suspense-placeholder.directive';
import { SuspenseDirective, SuspenseState } from './suspense.directive';

describe('SuspensePlaceholderDirective', () => {
  let directive: SuspensePlaceholderDirective;

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
    directive = new SuspensePlaceholderDirective(
      suspenseMock as any,
      templateRef as any,
      viewContainerRef as any,
      changeDeteciton as any,
    );
  });

  it('should be rendered when loading', () => {
    expect(directive.needsToBeRendered({ loading: true })).toEqual(true);
  });

  it('should use delay', fakeAsync(() => {
    (directive as any).render = jest.fn();
    directive.bbSuspensePlaceholderDelay = 1000;
    directive.ngOnInit();
    stateMock.next({ loading: true });
    expect((directive as any).render).not.toHaveBeenCalled();
    tick(500);
    expect((directive as any).render).not.toHaveBeenCalled();
    tick(501);
    expect((directive as any).render).toHaveBeenCalled();
  }));
});
