import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Message, User } from '../helpers/common-interfaces';

@Injectable({
  providedIn: 'root'
})
export class LivechatService {

  constructor(private http: HttpClient) { }

  findAllMessagesForUser(userEmail: string) : Observable<Message[]>{
   
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", userEmail);
    queryParams = queryParams.append("format", "json");
    
    return this.http.get<Message[]>(environment.apiURL + "/message/get-all-for-user", { params: queryParams});
  }

  getAllUsersFromMessages() : Observable<User[]>{
   
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    
    return this.http.get<User[]>(environment.apiURL + "/message/get-users", { params: queryParams});
  }
}
