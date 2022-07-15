import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { LoadingIndicatorModule } from '@backbase/ui-ang';
import { MockModule } from 'ng-mocks';
import { of } from 'rxjs';
import { DsCallActionAngComponent } from '../src/ds-call-action-ang.component';

describe('DsCallActionAngComponent', () => {
  let component: DsCallActionAngComponent;
  let fixture: ComponentFixture<DsCallActionAngComponent>;

  const flowInteractionServiceMock = {
    nav: {
      currentStep: { name: 'create-case' },
      nextStep: { name: 'select-products' },
      steps: ['create-case', 'company-lookup'],
      next: jest.fn(),
    },
    call: jest.fn().mockReturnValue(
      of({
        steps: {
          'create-case': { back: 'done' },
          'company-lookup': { back: 'create-case' },
        },
      }),
    ),
  };

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [MockModule(LoadingIndicatorModule)],
        declarations: [DsCallActionAngComponent],
        providers: [
          {
            provide: FlowInteractionService,
            useValue: flowInteractionServiceMock,
          },
        ],
      }).compileComponents();
    }),
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(DsCallActionAngComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate next after successfully calling of the action handler', () => {
    component.actionName = 'some-action-name';
    fixture.detectChanges();
    component.ngOnInit();

    expect(flowInteractionServiceMock.nav.next).toHaveBeenCalled();
  });
});
