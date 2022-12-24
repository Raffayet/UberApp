import { TestBed } from '@angular/core/testing';

import { LivechatService } from './livechat.service';

describe('LivechatService', () => {
  let service: LivechatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LivechatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
