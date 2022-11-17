import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { FormGroup } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  logIn(credentials: FormGroup): void {
    let data = {
      email: credentials.value.email,
      password: credentials.value.password
    }
    this.http.post("http://localhost:8081/api/auth/login", data)
      .subscribe(res => {console.log(data)})
  }
}
