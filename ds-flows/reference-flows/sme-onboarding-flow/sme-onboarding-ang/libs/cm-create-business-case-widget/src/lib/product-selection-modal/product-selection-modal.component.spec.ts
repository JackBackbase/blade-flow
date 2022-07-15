import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import {
  ButtonModule,
  DropdownSingleSelectModule,
  InputRadioGroupModule,
  InputTextModule,
  InputValidationMessageModule,
  LoadButtonModule,
  ModalModule,
} from '@backbase/ui-ang';
import { of } from 'rxjs';
import { ProductSelectionModalComponent } from './product-selection-modal.component';

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

describe('ProductSelectionModalComponent', () => {
  let component: ProductSelectionModalComponent;
  let fixture: ComponentFixture<ProductSelectionModalComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [
          ModalModule,
          ButtonModule,
          DropdownSingleSelectModule,
          InputTextModule,
          InputValidationMessageModule,
          ReactiveFormsModule,
          InputRadioGroupModule,
          LoadButtonModule,
        ],
        declarations: [ProductSelectionModalComponent],
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
    fixture = TestBed.createComponent(ProductSelectionModalComponent);
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

  it('onConfirmDialog should return undefined if form is not valid', () => {
    const result = component.onConfirmDialog();

    expect(result).toEqual(undefined);
  });

  it('onConfirmDialog should call _flowInteractionService if form is valid', () => {
    component.productSelectionForm.setValue({
      product: 'test',
    });
    component.onConfirmDialog();

    expect(flowInteractionServiceMock.call).toHaveBeenCalled();
  });
});
