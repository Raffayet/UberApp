import { TestBed } from '@angular/core/testing';

import { DriverGuard } from './driver.guard';

describe('DriverGuard', () => {
  let guard: DriverGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(DriverGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
