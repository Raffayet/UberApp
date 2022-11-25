import { environment } from './../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Register } from '../model/register';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(private http: HttpClient) { }

  register(registrationForm:Register):Observable<any>{
    console.log(registrationForm);

    return this.http.post(environment.apiURL+"user/",registrationForm);
  }
}
