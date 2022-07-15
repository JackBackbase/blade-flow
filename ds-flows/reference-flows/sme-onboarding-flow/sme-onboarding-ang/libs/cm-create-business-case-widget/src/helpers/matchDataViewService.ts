import { Injectable } from '@angular/core';
import { BadgeColor } from '@backbase/ui-ang';
import { CaseStatusEnum } from '../lib/model/MatchDataView';

@Injectable({
  providedIn: 'root',
})
export class MatchDataViewService {
  matchEnum = CaseStatusEnum;
  matchData = new Map<number, { text: string; color: BadgeColor }>([
    [CaseStatusEnum.OPEN, { text: 'Open', color: 'info' }],
    [CaseStatusEnum.PENDING, { text: 'Completed', color: 'warning' }],
    [CaseStatusEnum.APPROVED, { text: 'Signed', color: 'success' }],
    [CaseStatusEnum.REJECTED, { text: 'Awaiting', color: 'danger' }],
    [CaseStatusEnum.CANCELLED, { text: 'Cancelled', color: 'danger' }],
  ]);

  public matchDataColor = (value: any) => {
    return this.matchData.get(+this.matchEnum[value])?.color || '';
  };

  public matchDataText = (value: any) => {
    return this.matchData.get(+this.matchEnum[value])?.text || '';
  };
}
