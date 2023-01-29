import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { vehicleTypes } from '../mocks/register-driver';

import { DriverService } from './driver.service';

describe('DriverService', () => {
  let service: DriverService;
  let httpTestingController: HttpTestingController;

  let url = 'http://localhost:8081/api';

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(DriverService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get vehicle types and return the vehicle types', () => {

    service.getVehicleTypes().subscribe((res) => {
      expect(res[0]).toEqual("Standard");
    });

    const req = httpTestingController.expectOne({
      method: 'GET',
      url: `${url}/vehicleType/`,
    });

    req.flush(vehicleTypes);

  });
  

});
