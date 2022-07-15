import { TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { NotificationService } from '@backbase/ui-ang';
import { of } from 'rxjs';
import { CmCreateBusinessCaseWidgetService } from './cm-create-business-case-widget.service';
import { businessInformations } from './lib/model/business-informations.model';
import { Product } from './lib/model/product.model';

const flowInteractionServiceMock = {
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
  call: jest.fn().mockReturnValue(
    of({
      data: {
        body: [
          {
            name: 'test',
            referenceId: 'test',
          },
        ],
      },
    }),
  ),
};

const notoficationServiceMock = {
  showNotification: jest.fn().mockReturnValue(of('test')),
};

describe('CmCreateBusinessCaseWidgetService', () => {
  let service: CmCreateBusinessCaseWidgetService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CmCreateBusinessCaseWidgetService,
        {
          provide: FlowInteractionService,
          useValue: flowInteractionServiceMock,
        },
        {
          provide: NotificationService,
          useValue: notoficationServiceMock,
        },
      ],
    });
    service = TestBed.inject(CmCreateBusinessCaseWidgetService);
  });

  it('should create', () => {
    expect(service).toBeTruthy();
  });

  it('getUsaStates should return list of usa states', () => {
    service.getUsaStates().subscribe((result) => {
      expect(result).toEqual([
        {
          name: 'Arizona',
          isoCode: 'US-AZ',
          code: 'AZ',
        },
      ]);
    });
  });

  it('setBusinessInformationsFormValue should emit proper value', () => {
    service.setBusinessInformationsFormValue({ firstName: 'test' } as businessInformations);

    service.businessInformationsData$.subscribe((result) => {
      expect(result).toEqual({ firstName: 'test' });
    });
  });

  it('getBusinessInformationsFormValue should return proper businessStructure value', () => {
    const businessInformationsData: businessInformations = {
      firstName: 'test',
      lastName: 'test',
      businessName: 'test',
      numberAndStreet: 'test',
      city: 'Bytom',
      state: 'SY',
      zipCode: 'test',
      emailAddress: 'test@test.pl',
    };

    service.setBusinessInformationsFormValue(businessInformationsData);

    const result = service.getBusinessInformationsFormValue();
    expect(result).toEqual(businessInformationsData);
  });

  it('pushNotification should trigget notificationService showNotification method', () => {
    service.pushNotification('message', 'success');

    expect(notoficationServiceMock.showNotification).toHaveBeenCalled();
  });

  it('getProductList should return the list of products', () => {
    const result = service.getProductList();
    const expected = [
      {
        name: 'test',
        referenceId: 'test',
      },
    ] as Product[];

    result.subscribe((data) => {
      expect(data).toEqual(expected);
    });
  });
});
