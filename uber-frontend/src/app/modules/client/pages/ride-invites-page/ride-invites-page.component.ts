import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/app/environments/environment';
import { RideInvite } from 'src/app/model/RideInvite';
import { User } from 'src/app/model/User';
import { LivechatService } from 'src/app/modules/shared/services/livechat.service';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { ClientService } from '../../services/client.service';
import { over, Client, Message as StompMessage} from 'stompjs';
import { Ride } from 'src/app/model/Ride';

@Component({
  selector: 'app-requests-page',
  templateUrl: './ride-invites-page.component.html',
  styleUrls: ['./ride-invites-page.component.css']
})
export class RideInvitesPageComponent {
  stompClient: any;
  loggedUser: User | null;
  rideInvites: RideInvite[];

  constructor(private clientService: ClientService, private tokenUtilsService: TokenUtilsService) {}

  ngOnInit() {
    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);    
  }

  onConnected = () => {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();          
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/ride-invites", this.onRideInvitesReceived);

      this.clientService.findAllRideInvitesForUser(this.loggedUser?.email as string)
          .subscribe(val => this.rideInvites = val);     
  }

  onRideInvitesReceived = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);      
      this.rideInvites.push(payloadData);
  }

  onError = () => {
    console.log("Error");    
  }

  onAcceptRideInvite(index: number){
    this.stompClient.send('/app/ride-response', {}, JSON.stringify({
      email: this.rideInvites.at(index)?.emailFrom,
      responderEmail: this.loggedUser?.email,
      isAccepted: true
    }));

    this.clientService.changeDriveInvitationStatus(this.rideInvites.at(index)?.id as number, true).subscribe();
    this.rideInvites.splice(index, 1);
  }

  onRejectRideInvite(index: number){
    this.stompClient.send('/app/ride-response', {}, JSON.stringify({
      email: this.rideInvites.at(index)?.emailFrom,
      responderEmail: this.loggedUser?.email,
      isAccepted: false
    }));

    this.clientService.changeDriveInvitationStatus(this.rideInvites.at(index)?.id as number, false).subscribe();
    this.rideInvites.splice(index, 1);
  }

}
