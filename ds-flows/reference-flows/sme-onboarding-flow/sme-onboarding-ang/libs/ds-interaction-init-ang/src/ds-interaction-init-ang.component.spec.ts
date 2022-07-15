import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ActivatedRoute, Params } from '@angular/router';
import { AcActionService } from '@backbase/ac-common-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { ReplaySubject } from 'rxjs';
import { DsInteractionInitAngComponent } from './ds-interaction-init-ang.component';

class ActivatedRouteStub {
  private subject = new ReplaySubject<Params>(1);

  constructor(initialParams: Params = {}) {
    this.setParamMap(initialParams);
  }

  readonly queryParams = this.subject.asObservable();

  setParamMap(params: Params) {
    this.subject.next(params);
  }
}

const acActionServiveStub = {
  initialized: true,
};

describe('DsInteractionInitAngComponent', () => {
  const activateRoute = new ActivatedRouteStub();
  const dispatchResponse$ = new ReplaySubject<{}>(1);
  let component: DsInteractionInitAngComponent;
  let fixture: ComponentFixture<DsInteractionInitAngComponent>;
  let flowInteractionService: FlowInteractionService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DsInteractionInitAngComponent],
      schemas: [NO_ERRORS_SCHEMA],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: activateRoute,
        },
        {
          provide: FlowInteractionService,
          useValue: {
            dispatch: jest.fn().mockReturnValue(dispatchResponse$.asObservable()),
          },
        },
        {
          provide: AcActionService,
          useValue: acActionServiveStub,
        },
      ],
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DsInteractionInitAngComponent);
    component = fixture.componentInstance;
    flowInteractionService = TestBed.inject(FlowInteractionService);
  });

  describe('when the init action is being sent', () => {
    beforeEach(() => {
      activateRoute.setParamMap({ id: 'someId' });
    });

    it('should show the loading state', () => {
      const actionLoadingStateIdentifier = '*[data-role=action-loading-indicator]';

      component.isLoading$.next(true);
      fixture.detectChanges();
      const actionLoadingStateDe: DebugElement = fixture.debugElement.query(By.css(actionLoadingStateIdentifier));

      expect(actionLoadingStateDe).toBeTruthy();
    });
  });
});
