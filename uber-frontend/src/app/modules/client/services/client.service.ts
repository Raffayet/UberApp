import { RideRequest } from './../../../model/RideRequest';
import { MapSearchResult } from './map.service';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { RideInvite } from 'src/app/model/RideInvite';
import { User } from 'src/app/model/User';
import { over, Client, Message as StompMessage} from 'stompjs';
import { RideRequestStateService } from './ride-request-state.service';
import { CheckForEnoughTokens } from 'src/app/model/CheckForEnoughTokens';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  
  constructor(private http: HttpClient) { }
  
  createDriveInvitation(loggedUser: User | null, people: string[], locations: MapSearchResult[], priceToPay: number, stompClient: Client): Observable<String>{
    
    let params : RideInvite = {
      emailFrom: loggedUser?.email as string,
      emailsTo: people,
      firstLocation: locations[0].displayName,
      destination: locations[locations.length - 1].displayName,
      priceToPay: priceToPay,
      rideInviteStatus: 2
    }

    stompClient.send('/app/ride-invite', {}, JSON.stringify(params));
    return this.http.post<String>(environment.apiURL + "/client/create-drive-invitation", params);
  }

  changeDriveInvitationStatus(id: number, isAccepted: boolean): Observable<String>{
    return this.http.put<String>(environment.apiURL + "/client/change-drive-invitation-status", {id: id, isAccepted: isAccepted});
  }

  findAllRideInvitesForUser(userEmail: string) : Observable<RideInvite[]>{
   
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", userEmail);
    queryParams = queryParams.append("format", "json");
    
    return this.http.get<RideInvite[]>(environment.apiURL + "/client/get-ride-invites", { params: queryParams});
  }

  submitRequest(request: RideRequest): Observable<String> {
      let headers = new HttpHeaders();
      return this.http.post<String>(environment.apiURL + "/client/create-drive-request", request, { headers, responseType: 'text' as 'json' });
  }

  submitReservation(request: RideRequest): Observable<String> {
    request.isReserved = true;
    request.timeOfRequestForReservation = new Date();

    let headers = new HttpHeaders();
    return this.http.post<String>(environment.apiURL + "/client/create-reservation-drive-request", request, { headers, responseType: 'text' as 'json' });
}

  invitedHasTokens(initiatorEmail: string, peopleEmails: string[], pricePerPassenger: number): Observable<String> {
    let checkForEnoughTokens: CheckForEnoughTokens = {
      initiatorEmail: initiatorEmail,
      peopleEmails: peopleEmails,
      pricePerPassenger: pricePerPassenger
    }

    let headers = new HttpHeaders();

    return this.http.post<String>(environment.apiURL + "/client/invited-has-money", checkForEnoughTokens, { headers, responseType: 'text' as 'json' });
  }

  refundTokens(requestId: number): Observable<String>{
    let headers = new HttpHeaders();
    return this.http.post<String>(environment.apiURL + "/client/refund-tokens", requestId, { headers, responseType: 'text' as 'json' });
  }

  refundTokensAfterAccepting(requestId: number): Observable<String>{
    let headers = new HttpHeaders();
    return this.http.post<String>(environment.apiURL + "/client/refund-tokens-after-accepting", requestId, { headers, responseType: 'text' as 'json' });
  }
}
