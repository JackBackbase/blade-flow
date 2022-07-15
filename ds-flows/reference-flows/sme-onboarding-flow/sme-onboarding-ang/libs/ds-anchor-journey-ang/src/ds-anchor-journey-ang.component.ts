import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-sdk-ang/core';
import { FormHelperFactory } from '@backbase/ono-common-ang';
import { combineLatest } from 'rxjs';
import { take } from 'rxjs/operators';
import { AnchorFormMetadata } from './anchor-form';

@Component({
  selector: 'bb-ds-anchor-journey-ang',
  templateUrl: 'ds-anchor-journey-ang.component.html',
  styleUrls: ['ds-anchor-journey-ang.component.scss'],
  providers: [DatePipe, FormHelperFactory],
})
export class DsAnchorJourneyAngComponent {
  readonly helper = this.helperFactory
    .getHelper()
    .setFieldsConfig(AnchorFormMetadata)
    .setModel({ dateOfBirth: undefined })
    .setPayloadMapper((formValue) => ({
      ...formValue,
      dateOfBirth: this._datePipe.transform(formValue.dateOfBirth, 'yyyy-MM-dd'),
    }));

  constructor(
    public readonly helperFactory: FormHelperFactory,
    private readonly _flowInteraction: FlowInteractionService,
    private readonly _datePipe: DatePipe,
  ) {
    combineLatest([
      _flowInteraction.cdo.get<string>('firstName').pipe(take(1)),
      _flowInteraction.cdo.get<string>('lastName').pipe(take(1)),
      _flowInteraction.cdo.get<string>('dateOfBirth').pipe(take(1)),
      _flowInteraction.cdo.get<string>('email').pipe(take(1)),
    ])
      .pipe(take(1))
      .subscribe(([firstName, lastName, dateOfBirth, email]) => {
        const dob = dateOfBirth ? new Date(dateOfBirth) : '';

        this.helper.setModel({ firstName, lastName, dateOfBirth: dob, email });
      });
  }

  async submitAction() {
    try {
      const response = await this.helper.submit('sme-onboarding-anchor-data');
      if (!response?.actionErrors?.length) {
        this._flowInteraction.nav.next();
      } else {
        throw new Error(`SME Onboarding Anchor Data error: ${response.actionErrors[0].message}`);
      }
    } catch (e) {
      console.error(e);
    }
  }
}
