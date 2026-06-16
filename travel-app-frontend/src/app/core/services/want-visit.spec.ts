import { TestBed } from '@angular/core/testing';

import { WantVisitService } from './want-visit';

describe('WantVisit', () => {
  let service: WantVisitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WantVisitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
