import { DOCUMENT } from '@angular/common';
import { ChangeDetectorRef, Component, Inject, Input, OnDestroy, OnInit } from '@angular/core';
import { FlowInteractionService } from '@backbase/flow-interaction-core-ang';
import { Observable, of, Subject } from 'rxjs';
import { catchError, map, publishReplay, refCount, takeUntil } from 'rxjs/operators';

interface LandingJourneyCDO {
  email: string;
  caseId: string;
}

@Component({
  selector: 'bb-ds-landing-journey-ang',
  templateUrl: 'ds-landing-journey-ang.component.html',
  styleUrls: ['ds-landing-journey-ang.component.scss'],
})
export class DsLandingJourneyAngComponent implements OnInit, OnDestroy {
  @Input() linkNavigateTo?: string;
  @Input() heading?: string;
  emailText? = 'the email you provided';

  private userLandingCaseData$ = this.fetchUserLandingCaseData();
  private caseId = '';
  private onDestroy$ = new Subject();

  constructor(
    private readonly flowInteractionService: FlowInteractionService,
    private ref: ChangeDetectorRef,
    // Metadata can't contain ambient type
    // The same approach can be found in Angular sources
    // https://github.com/angular/angular/blob/master/packages/common/src/location/platform_location.ts#L115
    @Inject(DOCUMENT) private readonly document: any,
  ) {}

  ngOnInit(): void {
    // Get Case Id
    this.userLandingCaseData$.pipe(takeUntil(this.onDestroy$)).subscribe({
      next: (data) => {
        this.caseId = data?.caseId || '';
        this.emailText = data?.email;
        this.ref.detectChanges();
      },
    });
  }

  onButtonClick(linkNavigateTo: string): void {
    const urlSuffix = `${linkNavigateTo}?id=${this.caseId}`;
    const localPath = 'http://localhost:4202';
    const url = this.document.location.hostname === 'localhost' ? localPath + urlSuffix : urlSuffix;
    this.document.location.assign(url);
  }

  private fetchUserLandingCaseData(): Observable<LandingJourneyCDO | undefined> {
    return this.flowInteractionService
      .call({
        action: 'sme-onboarding-landing-data',
        body: {},
      })
      .pipe(
        map((response) => response.body),
        publishReplay(1),
        refCount(),
        catchError(() => of(undefined)),
      );
  }

  ngOnDestroy(): void {
    this.onDestroy$.next();
    this.onDestroy$.complete();
    // Clearing the userCaseData
    this.userLandingCaseData$ = of(undefined);
  }
}
