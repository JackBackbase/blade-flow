import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { BehaviorSubject, Subject } from 'rxjs';
import { createSpyObj } from '../../../createSpy';
import { DsLandingJourneyAngComponent } from './ds-landing-journey-ang.component';

describe('DsLandingJourneyAngComponent', () => {
  let fixture: ComponentFixture<DsLandingJourneyAngComponent>;
  let component: DsLandingJourneyAngComponent;

  let handlerResponse$: Subject<{ body?: { email: string } }>;

  beforeEach(() => {
    handlerResponse$ = new BehaviorSubject<{ body?: { email: string } }>({ body: { email: 'some@email.com' } });

    const flowInteractionService = {
      call: () => handlerResponse$,
    };

    fixture = TestBed.configureTestingModule({
      declarations: [DsLandingJourneyAngComponent],
      providers: [{ provide: FlowInteractionService, useValue: flowInteractionService }],
      schemas: [NO_ERRORS_SCHEMA],
    }).createComponent(DsLandingJourneyAngComponent);

    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  describe('if the handler fails', () => {
    it('should not cause an error', () => {
      expect(() => handlerResponse$.error(new Error('some error'))).not.toThrow();
    });
  });

  describe('if the handler does not return body', () => {
    it('should not cause an error', () => {
      expect(() => handlerResponse$.next({})).not.toThrow();
    });
  });

  describe('if linkNavigateTo input is set', () => {
    let button: HTMLElement;

    beforeEach(() => {
      fixture.detectChanges();

      button = fixture.nativeElement.querySelector('bb-load-button-ui');
    });

    it('should navigate to the provided link', () => {
      const location = createSpyObj('Location', ['assign']);

      // The simplest and working way to mock location
      // it's impossible to mock Angular's DOCUMENT (error _doc.querySelectorAll is not a function)
      // it's impossible to spy on Location.assign
      // it's impossible to use @ng-web-apis/common's WINDOW since ngc can't find Window type
      (component as any).document = { location };
      component.linkNavigateTo = 'some-link';
      fixture.detectChanges();
      button = fixture.nativeElement.querySelector('bb-load-button-ui');

      button.click();

      expect(location.assign).toHaveBeenCalledWith('some-link?id=');
    });
  });
});
