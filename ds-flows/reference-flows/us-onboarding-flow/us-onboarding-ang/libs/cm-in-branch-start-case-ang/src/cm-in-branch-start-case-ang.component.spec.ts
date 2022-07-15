import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { PAGE_CONFIG } from '@backbase/foundation-ang/web-sdk';

import { CmInBranchStartCaseAngComponent } from './cm-in-branch-start-case-ang.component';

describe('CmInBranchStartCaseAngComponent', () => {
  let component: CmInBranchStartCaseAngComponent;
  let fixture: ComponentFixture<CmInBranchStartCaseAngComponent>;
  let fakeFlowInteractionService: Pick<FlowInteractionService, 'init' | 'call'>;

  beforeEach(async () => {
    fakeFlowInteractionService = jasmine.createSpyObj('FlowInteractionService', {
      init: jasmine.createSpy('init').and.callThrough(),
      call: jasmine.createSpy('call').and.callThrough(),
    });

    await TestBed.configureTestingModule({
      declarations: [CmInBranchStartCaseAngComponent],
      providers: [
        { provide: FlowInteractionService, useValue: fakeFlowInteractionService },
        {
          provide: PAGE_CONFIG,
          useValue: {
            apiRoot: '/gateway/api/',
          },
        },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(CmInBranchStartCaseAngComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.currentStep).toBe('select-products');
    expect(component.isModalOpen).toBeFalsy();
  });

  it('should initialise the service and open the modal', () => {
    component.openModal();
    expect(fakeFlowInteractionService.init).toHaveBeenCalled();
    expect(component.isModalOpen).toBeTruthy();
  });

  it('should go to next step', () => {
    component.goToStep('personal-info');
    expect(component.currentStep).toBe('personal-info');
  });

  it('should close the modal', () => {
    component.closeModal();
    expect(component.currentStep).toBe('select-products');
    expect(component.isModalOpen).toBeFalsy();
  });
});
