import { TemplateRef, ViewContainerRef } from '@angular/core';
import { fakeAsync, tick } from '@angular/core/testing';
import { Subject } from 'rxjs';
import { SuspenseBaseDirective } from './suspense-base-directive';
import { SuspenseDirective, SuspenseState } from './suspense.directive';

describe('SuspenseBaseDirective', () => {
  let directive: SuspenseBaseDirective;

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

    changeDeteciton = {
      markForCheck: jest.fn(),
    };
    viewContainerRef = {
      createEmbeddedView: jest.fn(),
      clear: jest.fn(),
    };
    templateRef = {};

    directive = new SuspenseBaseDirective(
      suspenseMock as any,
      templateRef as any,
      viewContainerRef as any,
      changeDeteciton as any,
    );
  });

  it('should render on state changes', fakeAsync(() => {
    (directive as any).render = jest.fn();
    directive.ngOnInit();
    stateMock.next({});
    tick(100);
    expect((directive as any).render).toHaveBeenCalled();
  }));

  it('should unsubscribe from input on destroy', () => {
    directive.ngOnInit();
    expect(stateMock.observers.length).toEqual(1);
    directive.ngOnDestroy();
    expect(stateMock.observers.length).toEqual(0);
  });

  it('should set template $implicit as data by default', () => {
    expect(directive.getTemplateContext({ data: 'foo' })).toEqual({ $implicit: 'foo' });
  });

  it('should mark component for change detection on render', fakeAsync(() => {
    directive.ngOnInit();
    directive.needsToBeRendered = () => true;
    stateMock.next({ data: 'foo' });
    tick(1);
    expect(changeDeteciton.markForCheck).toHaveBeenCalled();
  }));

  it('needsToBeRendered should throw an error', fakeAsync(() => {
    expect(directive.needsToBeRendered).toThrow(new Error('needsToBeRendered method should be owerwriten'));
  }));
});
