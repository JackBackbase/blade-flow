import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { FormBuilder } from '@backbase/ono-journey-collection-ang/common';
import { FormlyFormBuilder } from '@ngx-formly/core';
import { of } from 'rxjs';
import { ProductSelectionComponent } from './product-selection.component';

xdescribe('ProductSelectionComponent', () => {
  let component: ProductSelectionComponent;
  let fixture: ComponentFixture<ProductSelectionComponent>;
  const flowInteractionServiceMock: Pick<FlowInteractionService, 'init' | 'call'> = {
    call: jasmine.createSpy('call').and.returnValue(of()),
    init: jasmine.createSpy('init'),
  };
  const formlyFormBuilder: Pick<FormlyFormBuilder, 'buildForm'> = {
    buildForm: jasmine.createSpy('buildForm'),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductSelectionComponent],
      providers: [
        { provide: FlowInteractionService, useValue: flowInteractionServiceMock },
        {
          provide: FormBuilder,
          useValue: formlyFormBuilder,
        },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductSelectionComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
