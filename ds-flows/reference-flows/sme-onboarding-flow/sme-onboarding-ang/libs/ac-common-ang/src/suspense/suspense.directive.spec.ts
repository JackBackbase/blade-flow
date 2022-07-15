import { SuspenseDirective } from './suspense.directive';
import { take } from 'rxjs/operators';
import { Subject } from 'rxjs';

describe('SuspenseDirective', () => {
  let directive: SuspenseDirective;
  let testInput: Subject<any>;

  beforeEach(() => {
    directive = new SuspenseDirective();
    testInput = new Subject();
    directive.bbSuspense = testInput;
  });

  it('should set initial state as loading', async () => {
    const state = await directive.state$.pipe(take(1)).toPromise();
    expect(state.loading).toBe(true);
  });

  it('should set error state on error', async () => {
    testInput.error({ foo: 'bar' });
    const state = await directive.state$.pipe(take(1)).toPromise();
    expect(state.loading).toBeFalsy();
    expect(state.error).toEqual({ foo: 'bar' });
  });

  it('should set data on success', async () => {
    testInput.next({ foo: 'bar' });
    const state = await directive.state$.pipe(take(1)).toPromise();
    expect(state.loading).toBeFalsy();
    expect(state.data).toEqual({ foo: 'bar' });
  });

  it('should unsubscribe when directive destroyed', () => {
    expect(testInput.observers.length).toEqual(1);
    directive.ngOnDestroy();
    expect(testInput.observers.length).toEqual(0);
  });
});
