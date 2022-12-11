import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  constructor(private http: HttpClient) { }

  getVehicleTypes(){
    let headers = new HttpHeaders();

    return this.http.get<string[]>(environment.apiURL + '/vehicleType/');    
  }
}
