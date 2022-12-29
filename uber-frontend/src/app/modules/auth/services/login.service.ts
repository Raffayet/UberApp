import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry, map } from 'rxjs/operators';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  error = '';

  constructor(private http: HttpClient) { }

  logIn(credentials: FormGroup, success: boolean) : Observable<any> {

    let data = {
      email: credentials.value.email,
      password: credentials.value.password
    }

    let result = undefined;

    return this.http.post("http://localhost:8081/api/auth/login", data)
  }

  socialLogIn(email: string) : Observable<any> {

    let result = undefined;
    console.log(email)
    return this.http.post("http://localhost:8081/api/auth/socialLogin", email)
  }
}