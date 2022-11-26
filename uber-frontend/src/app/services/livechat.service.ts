import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Message } from '../components/livechat/livechat.component';

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
}
