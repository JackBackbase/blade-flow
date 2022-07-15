import { TestBed } from '@angular/core/testing';
import { MatchDataViewService } from './matchDataViewService';

describe('MatchDataViewService', () => {
  let service: MatchDataViewService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MatchDataViewService],
    });

    service = TestBed.inject(MatchDataViewService);
  });

  it('matchDataColor should return empty string if there is no associated data', () => {
    const result = service.matchDataColor(0);

    expect(result).toBe('');
  });

  it('matchDataText should return empty string if there is no associated data', () => {
    const result = service.matchDataText(0);

    expect(result).toBe('');
  });
});
