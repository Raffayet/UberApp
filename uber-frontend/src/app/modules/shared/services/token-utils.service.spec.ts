import { TestBed } from '@angular/core/testing';

import { TokenUtilsService } from './token-utils.service';

describe('TokenUtilsService', () => {
  let service: TokenUtilsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenUtilsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
