import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { AcCommonAngModule } from '@backbase/ac-common-ang';
import { InputDividedDateUiModule } from '@backbase/ds-shared-ang/ui';
import { CaseDefinitionsService } from '@backbase/flow-casedata-openapi-data';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { PAGE_CONFIG } from '@backbase/foundation-ang/web-sdk';
import {
  BadgeModule,
  ButtonModule,
  HeaderModule,
  IconModule,
  InputRadioGroupModule,
  InputTextModule,
  InputValidationMessageModule,
  LoadButtonModule,
  ModalModule,
  NotificationService,
} from '@backbase/ui-ang';
import { MockModule } from 'ng-mocks';
import { of } from 'rxjs';
import { CmCreateBusinessCaseWidgetComponent } from './cm-create-business-case-widget.component';
import { BusinessInformationsModalComponent } from './lib/business-informations-modal/business-informations-modal.component';
import { ExsistingCaseModalComponent } from './lib/exsisting-case-modal/exsisting-case-modal.component';
import { ProductSelectionModalComponent } from './lib/product-selection-modal/product-selection-modal.component';

class CaseDefinitionsServiceStub {}

const flowInteractionServiceMock = {
  call: jest.fn().mockReturnValue(
    of({
      data: {
        test: 'test',
      },
    }),
  ),
  init: () => {},
  getCollection: jest.fn().mockReturnValue(
    of({
      items: [
        {
          name: 'pl',
          isoCode: 'pl',
          states: [],
        },
        {
          name: 'US',
          isoCode: 'US',
          states: [
            {
              name: 'Arizona',
              isoCode: 'US-AZ',
              code: 'AZ',
            },
          ],
        },
      ],
    }),
  ),
};

const notoficationServiceMock = {
  showNotification: jest.fn().mockReturnValue(of('test')),
};

describe('CmCreateBusinessCaseWidgetComponent', () => {
  let component: CmCreateBusinessCaseWidgetComponent;
  let fixture: ComponentFixture<CmCreateBusinessCaseWidgetComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [
          MockModule(ModalModule),
          MockModule(ButtonModule),
          MockModule(HeaderModule),
          MockModule(BadgeModule),
          MockModule(IconModule),
          MockModule(InputValidationMessageModule),
          MockModule(InputDividedDateUiModule),
          MockModule(InputTextModule),
          MockModule(InputRadioGroupModule),
          MockModule(AcCommonAngModule),
          MockModule(LoadButtonModule),
          ReactiveFormsModule,
        ],
        declarations: [
          CmCreateBusinessCaseWidgetComponent,
          CmCreateBusinessCaseWidgetComponent,
          ExsistingCaseModalComponent,
          BusinessInformationsModalComponent,
          ProductSelectionModalComponent,
        ],
        providers: [
          {
            provide: CaseDefinitionsService,
            useClass: CaseDefinitionsServiceStub,
          },
          {
            provide: FlowInteractionService,
            useValue: flowInteractionServiceMock,
          },
          {
            provide: NotificationService,
            useValue: notoficationServiceMock,
          },
          {
            provide: PAGE_CONFIG,
            useValue: {
              apiRoot: '/gateway/api/',
            },
          },
        ],
      }).compileComponents();
    }),
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(CmCreateBusinessCaseWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('openCreateNewCaseModal should update isCreateNewCaseModalOpen value', () => {
    const openCancelRequestValue = true;
    component.openCreateNewCaseModal(openCancelRequestValue);

    component.isCreateNewCaseModalOpen$.subscribe((value) => {
      expect(value).toBeTruthy();
    });
  });

  it('goToCreatedCase should emit caseKey', () => {
    jest.spyOn(component.caseInstanceKey, 'emit');

    component.goToCreatedCase('test');

    expect(component.caseInstanceKey.emit).toHaveBeenCalled();
  });

  it('goToStep should return undefined if there is no step', () => {
    const result = component.goToStep('');

    expect(result).toBe(undefined);
  });

  it('goToStep should update current step', () => {
    component.goToStep('test');

    expect(component.currentStep).toBe('test');
  });

  it('createNewCase should trigger interactionService init and open modal', () => {
    jest.spyOn(component, 'openCreateNewCaseModal');
    component.createNewCase();

    expect(component.openCreateNewCaseModal).toHaveBeenCalledWith(true);
  });

  it('getCaseKey should update caseKey', () => {
    component.getCaseKey('test');

    expect(component.caseKey).toEqual('test');
  });

  it('finish should open modal and trigger notification', () => {
    jest.spyOn(component, 'openCreateNewCaseModal');
    component.finish();

    expect(component.openCreateNewCaseModal).toHaveBeenCalledWith(false);
  });

  it('checkIsCaseExsist should always trigger call method', () => {
    jest.spyOn(component, 'goToStep');
    component.checkIsCaseExsist();

    expect(flowInteractionServiceMock.call).toHaveBeenCalled();
  });
});
