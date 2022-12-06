import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  updatePersonalInfo(infoForm: FormGroup) : Observable<string>{
      let headers = new HttpHeaders();
      let params = infoForm.getRawValue();

      return this.http.put<string>(environment.apiURL + '/user/update-personal-info', params, { headers, responseType: 'text' as 'json' });      
  }

  updatePassword(passwordForm: FormGroup, email: string): Observable<string>{
    let headers = new HttpHeaders();
    let params = {...passwordForm.getRawValue(), email: email};

    return this.http.put<string>(environment.apiURL + '/user/update-password', params, { headers, responseType: 'text' as 'json' });      
  }
}
