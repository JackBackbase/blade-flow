import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InputCheckboxModule } from '@backbase/ui-ang';
import { first } from 'rxjs/operators';
import { TermsAndConditionsStepComponent } from './terms-and-conditions-step.component';

describe('TermsAndConditionsStepComponent', () => {
  let component: TermsAndConditionsStepComponent;
  let fixture: ComponentFixture<TermsAndConditionsStepComponent>;

  beforeEach(() => {
    fixture = TestBed.configureTestingModule({
      imports: [FormsModule, ReactiveFormsModule, InputCheckboxModule],
      declarations: [TermsAndConditionsStepComponent],
      schemas: [NO_ERRORS_SCHEMA],
    }).createComponent(TermsAndConditionsStepComponent);

    component = fixture.componentInstance;
    component.tncLinkText = 'Terms and conditions';
    component.tncLink = 'http://tnc.com';
    component.privacyLinkText = 'Privacy Statement';
    component.privacyLink = 'http://privacy.com';

    fixture.detectChanges();
  });

  it('should display T&C link', () => {
    expect(fixture.nativeElement.textContent).toContain('Terms and conditions');
  });

  it('should have T&C link leading to T&C configured url', () => {
    const link = fixture.nativeElement.querySelector('[href="http://tnc.com"]');

    expect(link).not.toBeNull();
  });

  it('should display privacy link', () => {
    expect(fixture.nativeElement.textContent).toContain('Privacy Statement');
  });

  it('should have privacy link leading to privacy configured url', () => {
    const link = fixture.nativeElement.querySelector('[href="http://privacy.com"]');

    expect(link).not.toBeNull();
  });

  it('should have "complete" input set to false by default', () => {
    expect(component.complete).toBeFalsy();
  });

  it('should have checkbox value set to false by default', () => {
    expect(component.confirmCheckbox.value).toBeFalsy();
  });

  it('should set checkbox value equal to the "complete" input', () => {
    component.complete = true;

    component.ngOnChanges();

    expect(component.confirmCheckbox.value).toBeTruthy();
  });

  it('should emit "completeChange" output', () => {
    let checkbox = false;

    component.completeChange.pipe(first()).subscribe((x) => (checkbox = x));
    component.confirmCheckbox.setValue(true);

    expect(checkbox).toBeTruthy();
  });
});
