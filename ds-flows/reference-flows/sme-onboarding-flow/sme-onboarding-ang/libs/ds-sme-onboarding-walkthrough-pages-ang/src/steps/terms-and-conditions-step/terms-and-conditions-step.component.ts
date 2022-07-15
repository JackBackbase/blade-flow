import { Component, Input, OnChanges, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'bb-terms-and-conditions-step',
  templateUrl: 'terms-and-conditions-step.component.html',
  styleUrls: ['terms-and-conditions-step.component.scss'],
})
export class TermsAndConditionsStepComponent implements OnChanges {
  @Input() complete = false;
  readonly confirmCheckbox = new FormControl(this.complete);
  @Output() readonly completeChange = this.confirmCheckbox.valueChanges;

  @Input() privacyLink?: string;
  @Input() privacyLinkText?: string;
  @Input() tncLink?: string;
  @Input() tncLinkText?: string;

  ngOnChanges(): void {
    this.confirmCheckbox.setValue(this.complete);
  }
}
