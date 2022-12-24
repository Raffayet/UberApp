import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from "src/app/model/Message";
import { User } from "src/app/model/User";
import { environment } from 'src/app/environments/environment';

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

  getAdminChat(): Observable<Map<string, Message[]>>{
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    
    return this.http.get<Map<string, Message[]>>(environment.apiURL + "/message/get-admin-chat", { params: queryParams});
  }

  persistMessage(message: Message): void{
    this.http.post(environment.apiURL + "/message/save", message, {responseType: 'text'}).subscribe({
      next: data => {
          // console.log(data);
      },
      error: error => {
          // console.error('There was an error!', error);
      }
    });
  }

}
