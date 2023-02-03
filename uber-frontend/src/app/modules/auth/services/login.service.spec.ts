import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoginService } from './login.service';
import { filledLoginForm, tokenResponse } from '../mocks/login';
import { of } from 'rxjs';

describe('LoginService', () => {
  let service: LoginService;
  let httpTestingController: HttpTestingController;

  let url = 'http://localhost:8081/api';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(LoginService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login and return the appropriate token', () => {
    let success: boolean = false;

    service.logIn(filledLoginForm, success).subscribe((res) => {
      expect(res).toEqual(tokenResponse);
    });

    const req = httpTestingController.expectOne({
      method: 'POST',
      url: `${url}/auth/login`,
    });

    req.flush(tokenResponse);

  });


});
