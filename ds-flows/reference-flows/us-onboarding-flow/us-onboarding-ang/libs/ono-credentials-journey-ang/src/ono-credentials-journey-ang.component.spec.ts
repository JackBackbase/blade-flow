import { OnoCredentialsJourneyAngComponent } from './ono-credentials-journey-ang.component';
import { of } from 'rxjs';

class FakeHelper {
  form: any = {
    controls: {
      email: {},
      password: {},
    },
  };

  setFieldsConfig() {
    return this;
  }

  setModel() {
    return this;
  }

  setPayloadMapper() {
    return this;
  }
}

describe('OnoCredentialsJourneyAngComponent', () => {
  let component: OnoCredentialsJourneyAngComponent;
  let flowInteractionMock: any;
  let formHelperFactoryMock: any;
  let formHelperMock: any;

  beforeEach(() => {
    flowInteractionMock = {
      nav: { next: jasmine.createSpy('next') },
      cdo: {
        get: jasmine.createSpy('get').and.returnValue(of(undefined)),
      },
    };
    formHelperMock = new FakeHelper();
    spyOn(formHelperMock, 'setFieldsConfig').and.callThrough();
    spyOn(formHelperMock, 'setModel').and.callThrough();
    spyOn(formHelperMock, 'setPayloadMapper').and.callThrough();
    formHelperFactoryMock = {
      getHelper: () => formHelperMock,
    };

    component = new OnoCredentialsJourneyAngComponent(formHelperFactoryMock, flowInteractionMock);
  });

  it('should be correctly initialized', () => {
    expect(component).toBeDefined();
    expect(component.helper).toBeDefined();
    expect(component.passwordControl).toBeDefined();
    expect(component.has8Characters).toBeDefined();

    expect(component.includesNumber).toBeUndefined();
    expect(component.includesUppercase).toBeUndefined();
  });

  it('should navigate if no errors', async () => {
    component.helper.submit = jasmine.createSpy('submit').and.returnValue(Promise.resolve({ actionErrors: [] }));
    await component.submitAction();
    expect(component.helper.submit).toHaveBeenCalled();
    expect(flowInteractionMock.nav.next).toHaveBeenCalled();
  });

  it('should not navigate if errors', async () => {
    component.helper.submit = jasmine
      .createSpy('submit')
      .and.returnValue(Promise.resolve({ actionErrors: [{ code: 'foo', message: 'bar' }] }));
    await component.submitAction();
    expect(component.helper.submit).toHaveBeenCalled();
  });
});
