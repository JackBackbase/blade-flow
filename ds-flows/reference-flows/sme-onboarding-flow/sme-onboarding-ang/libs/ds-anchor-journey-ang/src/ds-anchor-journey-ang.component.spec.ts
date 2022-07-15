import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { FormBuilder, FormHelperFactory } from '@backbase/ono-common-ang';
import { MockProvider } from 'ng-mocks';
import { of } from 'rxjs';
import { DsAnchorJourneyAngComponent } from './ds-anchor-journey-ang.component';

describe('DsAnchorJourneyAngComponent', () => {
  let component: DsAnchorJourneyAngComponent;
  let fixture: ComponentFixture<DsAnchorJourneyAngComponent>;
  let flowInteractionService: FlowInteractionService;

  beforeEach(() => {
    flowInteractionService = {
      nav: { next: jest.fn() },
      cdo: {
        get: () => of(),
      },
    } as unknown as typeof flowInteractionService;

    fixture = TestBed.configureTestingModule({
      declarations: [DsAnchorJourneyAngComponent],
      providers: [
        { provide: FlowInteractionService, useValue: flowInteractionService },
        MockProvider(FormHelperFactory),
        MockProvider(FormBuilder, {
          buildForm: () => {},
        }),
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).createComponent(DsAnchorJourneyAngComponent);

    component = fixture.componentInstance;
  });

  describe('submitAction', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });

    it('should navigate if no errors', async () => {
      component.helper.submit = jest.fn().mockReturnValue(Promise.resolve({ actionErrors: [] }));
      await component.submitAction();
      expect(component.helper.submit).toHaveBeenCalled();
      expect(flowInteractionService.nav.next).toHaveBeenCalled();
    });

    it('should not navigate if errors', async () => {
      component.helper.submit = jest
        .fn()
        .mockReturnValue(Promise.resolve({ actionErrors: [{ code: 'foo', message: 'bar' }] }));
      await component.submitAction();
      expect(component.helper.submit).toHaveBeenCalled();
      expect(flowInteractionService.nav.next).not.toHaveBeenCalled();
    });
  });
});
