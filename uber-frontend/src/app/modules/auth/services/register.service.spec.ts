import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { badDriverRegisterData, goodDriverRegisterData } from '../mocks/register';
import { badUserRegisterData, goodUserRegisterData } from '../mocks/registerUser';

import { RegisterService } from './register.service';

describe('RegisterService', () => {
  let service: RegisterService;
  let httpTestingController: HttpTestingController;

  let url = 'http://localhost:8081/api';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(RegisterService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should register driver and return the appropriate string', () => {

    service.registerDriver(goodDriverRegisterData).subscribe((res) => {
      expect(res).toEqual("Success");
    });

    const req = httpTestingController.expectOne({
      method: 'POST',
      url: `${url}/driver/`,
    });

    req.flush("Success");

  });
  
  it('should fail register driver and return error', () => {

    service.registerDriver(badDriverRegisterData).subscribe((res) => {
      expect("Error").toEqual(res);
    });

    const req = httpTestingController.expectOne({
      method: 'POST',
      url: `${url}/driver/`,
    });

    req.flush("Error");

  });

  it('should register user and return the appropriate string', () => {

    service.register(goodUserRegisterData).subscribe((res) => {
      expect(res).toEqual("Success");
    });

    const req = httpTestingController.expectOne({
      method: 'POST',
      url: `${url}/user/`,
    });

    req.flush("Success");

  });

  it('should fail register driver and return error', () => {

    service.register(badUserRegisterData).subscribe((res) => {
      expect("Error").toEqual(res);
    });

    const req = httpTestingController.expectOne({
      method: 'POST',
      url: `${url}/user/`,
    });

    req.flush("Error");

  });

});
