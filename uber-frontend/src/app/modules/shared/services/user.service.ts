import { User } from 'src/app/model/User';
import { BlockUserRequest } from './../../../model/BlockUserRequest';
import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenUtilsService } from './token-utils.service';
import { environment } from 'src/app/environments/environment';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {
 

  constructor(private http: HttpClient, private tokenUtilsService: TokenUtilsService, private router: Router) { }

  getProfilePicture(email: string){
    let headers = new HttpHeaders();
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);

    return this.http.get<string>(environment.apiURL + '/user/profile-picture', {params:queryParams, headers, responseType: 'text' as 'json'});    
  }

  changeUserDrivingStatus(email:string, status: number){
    
    let headers = new HttpHeaders();
    let params = {email: email, status: status};       
    
    return this.http.post<string>(environment.apiURL + '/user/change-user-driving-status', params, {headers, responseType: 'text' as 'json'});    
  }

  updatePersonalInfo(infoForm: FormGroup) : Observable<string>{
    let headers = new HttpHeaders();
    let params = {...infoForm.getRawValue(), role: this.tokenUtilsService.getRoleFromToken()};

    return this.http.put<string>(environment.apiURL + '/client/update-personal-info', params, { headers, responseType: 'text' as 'json' });      
  }

  updatePassword(passwordForm: FormGroup, email: string): Observable<string>{
    let headers = new HttpHeaders();
    let params = {...passwordForm.getRawValue(), email: email};

    return this.http.put<string>(environment.apiURL + '/client/update-password', params, { headers, responseType: 'text' as 'json' });      
  }

  changeProfilePicture(email: string, b64Image: string | ArrayBuffer | null){    
    let headers = new HttpHeaders();
    let b64 = b64Image as string;    

    let params = {email: email, b64Image: b64.split(',', 2)[1]};       

    return this.http.put<string>(environment.apiURL + '/client/update-profile-picture', params, { headers, responseType: 'text' as 'json' });      
  }

  getUsers(){
    return this.http.get<string[]>(environment.apiURL + "/user/");
  }
  
  getNotBlockedUsers(){
    return this.http.get<string[]>(environment.apiURL + "/user/not-blocked");
  }

  blockUser(data:BlockUserRequest){

    return this.http.post<User>(environment.apiURL + "/user/block", data);
  }

  getUserTypeByEmail(email: string)
  {
    let headers = new HttpHeaders();
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);

    return this.http.get<string>(environment.apiURL + '/user/get-user-type', {params:queryParams, headers, responseType: 'text' as 'json'});
  }


  logOut() {
    localStorage.removeItem("user");
    this.router.navigateByUrl('/login');
  }

}
