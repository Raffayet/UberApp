import { TestBed } from '@angular/core/testing';

import { DriverServiceService } from './driver-service.service';

describe('DriverServiceService', () => {
  let service: DriverServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
