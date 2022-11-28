import { TestBed } from '@angular/core/testing';

import { AdditionalLoginInfoService } from './additional-login-info.service';

describe('AdditionalLoginInfoService', () => {
  let service: AdditionalLoginInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdditionalLoginInfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
