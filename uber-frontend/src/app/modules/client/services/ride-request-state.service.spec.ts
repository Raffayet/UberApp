import { TestBed } from '@angular/core/testing';

import { RideRequestStateService } from './ride-request-state.service';

describe('RideRequestStateService', () => {
  let service: RideRequestStateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RideRequestStateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
