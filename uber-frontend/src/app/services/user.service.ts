import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
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

  changeProfilePicture(email: string, b64Image: string | ArrayBuffer | null){    
    let headers = new HttpHeaders();
    let b64 = b64Image as string;    

    let params = {email: email, b64Image: b64.split(',', 2)[1]};       

    return this.http.put<string>(environment.apiURL + '/user/update-profile-picture', params, { headers, responseType: 'text' as 'json' });      
  }

  getProfilePicture(email: string){
    let headers = new HttpHeaders();
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);

    return this.http.get<string>(environment.apiURL + '/user/profile-picture', {params:queryParams, headers, responseType: 'text' as 'json'});    
  }

}
