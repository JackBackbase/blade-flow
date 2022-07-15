import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { FormBuilder } from '@backbase/ono-journey-collection-ang/common';
import { FormlyFormBuilder } from '@ngx-formly/core';
import { PersonalInformationComponent } from './personal-information.component';

describe('PersonalInformationComponent', () => {
  let component: PersonalInformationComponent;
  let fixture: ComponentFixture<PersonalInformationComponent>;
  const flowInteractionServiceMock: Pick<FlowInteractionService, 'call'> = {
    call: jasmine.createSpy('call'),
  };
  const formlyFormBuilder: Pick<FormlyFormBuilder, 'buildForm'> = {
    buildForm: jasmine.createSpy('buildForm'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PersonalInformationComponent],
      providers: [
        { provide: FlowInteractionService, useValue: flowInteractionServiceMock },
        {
          provide: FormBuilder,
          useValue: formlyFormBuilder,
        },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(PersonalInformationComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create personal information form', () => {
    expect(component.helper.form).toBeTruthy();
  });
});
