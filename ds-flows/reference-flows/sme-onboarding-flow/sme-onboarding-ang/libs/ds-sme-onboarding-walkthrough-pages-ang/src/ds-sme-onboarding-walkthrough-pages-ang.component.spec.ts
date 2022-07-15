import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ContentService } from '@backbase/content-ang';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { of } from 'rxjs';
import { SmeOnboardingWalkthroughPagesComponent } from './ds-sme-onboarding-walkthrough-pages-ang.component';

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

describe('SmeOnboardingWalkthroughPagesComponent', () => {
  let component: SmeOnboardingWalkthroughPagesComponent;
  let fixture: ComponentFixture<SmeOnboardingWalkthroughPagesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SmeOnboardingWalkthroughPagesComponent],
      providers: [
        {
          provide: FlowInteractionService,
          useValue: flowInteractionServiceMock,
        },
        {
          provide: ContentService,
          useValue: jest.fn().mockReturnValue(of({ content: 'some content' })),
        },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmeOnboardingWalkthroughPagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe('upon modal completion', () => {
    it('should be created', () => {
      expect(component).toBeTruthy();
    });

    it('Should send a request to find a business if form is valid', () => {
      component.onWalkthroughModalComplete();

      expect(flowInteractionServiceMock.call).toHaveBeenCalled();
    });
  });
});
