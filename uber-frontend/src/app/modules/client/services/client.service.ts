import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { RideInvite } from 'src/app/model/RideInvite';
import { User } from 'src/app/model/User';
import { Message } from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  
  createDriveInvitation(loggedUser: User | null, people: string[], inputValues: string[], priceToPay: number): Observable<String>{
    
    let params : RideInvite = {
      emailFrom: loggedUser?.email as string,
      emailsTo: people,
      firstLocation: inputValues[0],
      destination: inputValues[inputValues.length - 1],
      priceToPay: priceToPay,
      rideInviteStatus: 2
    }

    return this.http.post<String>(environment.apiURL + "/client/create-drive-invitation", params);
  }

  changeDriveInvitationStatus(id: number, isAccepted: boolean): Observable<String>{
    console.log(id)
    return this.http.put<String>(environment.apiURL + "/client/change-drive-invitation-status", {id: id, isAccepted: isAccepted});
  }

  findAllRideInvitesForUser(userEmail: string) : Observable<RideInvite[]>{
   
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", userEmail);
    queryParams = queryParams.append("format", "json");
    
    return this.http.get<RideInvite[]>(environment.apiURL + "/client/get-ride-invites", { params: queryParams});
  }

  constructor(private http: HttpClient) { }
}
