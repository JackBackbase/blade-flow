import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { ItemModel } from '@backbase/foundation-ang/core';
import { of } from 'rxjs';
import { AcHeaderWidgetComponent } from './ac-header-widget.component';

const flowInteractionServiceStub = {
  call: () => of({ body: { fullName: 'John Doe' } }),
};

const itemModelStub = {
  property: () => of({}),
};

describe('AcHeaderWidgetComponent', () => {
  let component: AcHeaderWidgetComponent;
  let fixture: ComponentFixture<AcHeaderWidgetComponent>;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AcHeaderWidgetComponent],
      providers: [
        {
          provide: FlowInteractionService,
          useValue: flowInteractionServiceStub,
        },
        {
          provide: ItemModel,
          useValue: itemModelStub,
        },
      ],
      schemas: [NO_ERRORS_SCHEMA],
    });
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AcHeaderWidgetComponent);
    component = fixture.componentInstance;
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should should call getRegistrar on init', () => {
    jest.spyOn<any, any>(component, 'getRegistrar');

    component.ngOnInit();

    expect((component as any).getRegistrar).toHaveBeenCalled();
  });
});
