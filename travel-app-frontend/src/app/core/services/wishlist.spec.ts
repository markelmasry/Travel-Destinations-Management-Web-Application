import { TestBed } from '@angular/core/testing';

import { WantVisitService } from './wishlist.service';

describe('WishlistItem', () => {
  let service: WantVisitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WantVisitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
