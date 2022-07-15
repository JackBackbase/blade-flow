import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AcCommonAngModule } from '@backbase/ac-common-ang';
import { CaseDataService } from '@backbase/cm-case-common-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import {
  BadgeModule,
  ButtonModule,
  DropdownSingleSelectModule,
  IconModule,
  InputRadioGroupModule,
  InputTextModule,
  InputValidationMessageModule,
  ModalModule,
} from '@backbase/ui-ang';
import { MockModule } from 'ng-mocks';
import { of } from 'rxjs';
import { ExsistingCaseModalComponent } from './exsisting-case-modal.component';

const flowInteractionServiceMock = {
  nav: {
    currentStep: { name: 'create-case' },
    nextStep: { name: 'select-products' },
    steps: ['create-case', 'company-lookup'],
    next: jest.fn(),
  },
  steps: jest.fn().mockReturnValue(
    of({
      'create-case': { back: 'done' },
      'company-lookup': { back: 'done' },
    }),
  ),
  call: jest.fn().mockReturnValue(
    of({
      steps: {
        'create-case': { back: 'done' },
        'company-lookup': { back: 'create-case' },
      },
      data: {},
    }),
  ),
  navigate: jest.fn().mockReturnValue(of({ body: [] })),
};

const caseDataServiceMock = {
  caseInstance$: of({}),
  loadCaseInstance: jest.fn(),
};

describe('ExsistingCaseModalComponent', () => {
  let component: ExsistingCaseModalComponent;
  let fixture: ComponentFixture<ExsistingCaseModalComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [
          MockModule(ModalModule),
          MockModule(ButtonModule),
          MockModule(DropdownSingleSelectModule),
          MockModule(InputTextModule),
          MockModule(InputValidationMessageModule),
          MockModule(InputRadioGroupModule),
          MockModule(IconModule),
          MockModule(AcCommonAngModule),
          MockModule(BadgeModule),
          ReactiveFormsModule,
        ],
        declarations: [ExsistingCaseModalComponent],
        providers: [
          {
            provide: CaseDataService,
            useValue: caseDataServiceMock,
          },
          {
            provide: FlowInteractionService,
            useValue: flowInteractionServiceMock,
          },
        ],
      }).compileComponents();
    }),
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(ExsistingCaseModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onCloseDialog should emit cancel and startFromBeginning', () => {
    jest.spyOn(component.cancel, 'emit');
    jest.spyOn(component.startFromBeginning, 'emit');

    component.onCloseDialog();

    expect(component.cancel.emit).toHaveBeenCalled();
    expect(component.startFromBeginning.emit).toHaveBeenCalled();
    expect(component.openDialog).toBeFalsy();
  });

  it('onGoToCase should emit goToExsistingCase', () => {
    jest.spyOn(component.goToExsistingCase, 'emit');

    component.onGoToCase();

    expect(component.goToExsistingCase.emit).toHaveBeenCalled();
  });
});
