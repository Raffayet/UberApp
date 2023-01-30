import { TestBed } from '@angular/core/testing';

import { SharedGuard } from './shared.guard';

describe('SharedGuard', () => {
  let guard: SharedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(SharedGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
