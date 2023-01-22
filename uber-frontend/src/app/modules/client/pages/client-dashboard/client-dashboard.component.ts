import { Component, Input, ViewChild, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { RideInvite } from 'src/app/model/RideInvite';
import { MatDialog } from '@angular/material/dialog';
import { RideInviteDialogComponent } from '../../components/ride-invite-dialog/ride-invite-dialog.component';
import { over, Client, Message as StompMessage} from 'stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/app/environments/environment';
import { ClientService } from '../../services/client.service';
import { ResponseToIniciator } from 'src/app/model/ResponseToIniciator';
import { RideReminder } from 'src/app/model/RideReminder';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;
    navbarLabels: string[] = ["Home", "Profile", "Ride Invites", "History", "Report"];
    navbarPaths: string[] = ["request-ride-page", "profile-page", "ride-invites", "history", "report"];
    option: string = this.navbarPaths[0];

    rideInvites: RideInvite[];
    stompClient: Client;
    loggedUser: User | null;
    
    changeOption(eventData: string): void{  
      this.router.navigate(['/client', {outlets: {'ClientRouter': [eventData]}}]);
    }

    constructor(private router: Router, private userService: UserService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private clientService: ClientService, private toastr: ToastrService){}

    ngOnInit() {
      let Sock = new SockJS(environment.apiURL + "/ws");
      this.stompClient = over(Sock);
      this.stompClient.connect({}, this.onConnected, this.onError); 
    }

    onConnected = () => {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();          
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/ride-invites", this.onRideInvitesReceived);
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/remind-for-ride", this.onRideReminder);
      this.stompClient.subscribe("/topic/response-to-other-clients", (data) => this.onResponseToOtherClients(data));

      this.clientService.findAllRideInvitesForUser(this.loggedUser?.email as string)
          .subscribe(val => this.rideInvites = val);  
    }

    onRideInvitesReceived = (payload: StompMessage) => {
        let payloadData = JSON.parse(payload.body);     
        this.rideInvites.push(payloadData);
    }

    onRideReminder = (payload: StompMessage) => {
      console.log(payload.body);
      let payloadData = JSON.parse(payload.body);
      let rideReminder: RideReminder;
      rideReminder = payloadData;
      this.toastr.info(`Reserved ride is going to take place in ${rideReminder.numberOfMinutes} minutes`); 
    }

    onResponseToClient = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      let responseToClient: ResponseToIniciator;
      responseToClient = payloadData;
      
      if(responseToClient.messageType === "driverRejected")
      {
        this.toastr.error(responseToClient.messageContent);
      }
    }

    onError = () => {
      console.log("Error");    
    }

    onResponseToOtherClients = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      let responseToClient: ResponseToIniciator;
      responseToClient = payloadData;
      console.log(responseToClient);
      if(responseToClient.messageType === "noDrivers")
      {
        this.toastr.error(responseToClient.messageContent);
      }

      else if(responseToClient.messageType === "driverRejected")
      {
        this.toastr.error(responseToClient.messageContent);
      }

      else{
        this.toastr.success(responseToClient.messageContent);
      }
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

    toggleChat = () => {
      this.chatHidden = !this.chatHidden;
    }

    logout = () => {      
      this.userService.changeUserDrivingStatus(this.tokenUtilsService.getUsernameFromToken() as string, 1).subscribe();
      localStorage.removeItem("user");
      this.router.navigateByUrl('/');
    }

    openDialog(index: number) {
      const dialogRef = this.dialog.open(RideInviteDialogComponent,{
        data:{
          from: `Invite from ${this.rideInvites[index]?.emailFrom}`,
          firstLocation: `${this.rideInvites[index]?.firstLocation}`,
          destination: `${this.rideInvites[index]?.destination}`,
          priceToPay: `${this.rideInvites[index]?.priceToPay} Tokens`,
          buttonText: {
            ok: 'Accept',
            cancel: 'Reject'
          }
        }
      });

      dialogRef.afterClosed().subscribe((confirmed: boolean) => {
        if (confirmed) {
            this.onAcceptRideInvite(index);
        }
        else{
            this.onRejectRideInvite(index);
        }
      });
    }
}
