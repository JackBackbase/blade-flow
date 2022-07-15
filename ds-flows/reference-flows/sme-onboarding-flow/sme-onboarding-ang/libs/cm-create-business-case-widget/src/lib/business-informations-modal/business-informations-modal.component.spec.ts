import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputDividedDateUiModule } from '@backbase/ds-ui-components-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import {
  ButtonModule,
  DropdownSingleSelectModule,
  InputTextModule,
  InputValidationMessageModule,
  LoadButtonModule,
  ModalModule,
} from '@backbase/ui-ang';
import { MockModule } from 'ng-mocks';
import { of } from 'rxjs';
import { CmCreateBusinessCaseWidgetService } from '../../cm-create-business-case-widget.service';
import { BusinessInformationsModalComponent } from './business-informations-modal.component';

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
    }),
  ),
  navigate: jest.fn().mockReturnValue(
    of({
      body: [],
    }),
  ),
};

describe('BusinessInformationsModalComponent', () => {
  let component: BusinessInformationsModalComponent;
  let fixture: ComponentFixture<BusinessInformationsModalComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [
          MockModule(ModalModule),
          MockModule(ButtonModule),
          MockModule(DropdownSingleSelectModule),
          MockModule(InputTextModule),
          MockModule(InputValidationMessageModule),
          MockModule(InputDividedDateUiModule),
          MockModule(LoadButtonModule),
          ReactiveFormsModule,
          FormsModule,
        ],
        declarations: [BusinessInformationsModalComponent],
        providers: [
          {
            provide: FlowInteractionService,
            useValue: flowInteractionServiceMock,
          },
          CmCreateBusinessCaseWidgetService,
        ],
      }).compileComponents();
    }),
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(BusinessInformationsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('onCloseDialod should close the modal', () => {
    jest.spyOn(component.cancel, 'emit');
    component.openDialog = true;
    component.onCloseDialog();
    expect(component.cancel.emit).toHaveBeenCalled();
    expect(component.openDialog).toBeFalsy();
  });

  it('onConfirmDialog should return undefined if form is not valid', () => {
    component.businessInformationsForm.controls.emailAddress.setErrors({ flow: true });
    const result = component.onConfirmDialog();

    expect(result).toEqual(undefined);
  });

  it('onConfirmDialog should not close modal if form is not valid', () => {
    jest.spyOn(component, 'onCloseDialog');
    component.onConfirmDialog();
    expect(component.onCloseDialog).not.toHaveBeenCalled();
  });

  it('allowToSubmit should return false if button should be disabled', () => {
    const result = component.allowToSubmit();

    expect(result).toBeFalsy();
  });
});
