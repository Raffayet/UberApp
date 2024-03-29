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
import { BlockUserRequest } from 'src/app/model/BlockUserRequest';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;
    navbarLabels: string[] = ["Home", "Profile", "History", "Report"];
    navbarPaths: string[] = ["request-ride-page", "profile-page", "history", "report"];
    option: string = this.navbarPaths[0];

    rideInvites: RideInvite[];
    stompClient: Client;
    loggedUser: User | null;
    
    changeOption(eventData: string): void{  
      this.router.navigate(['/client', {outlets: {'ClientRouter': [eventData]}}]);
    }

    constructor(private router: Router, private userService: UserService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private clientService: ClientService, private toastr: ToastrService){}

    ngOnInit() {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();  
      let Sock = new SockJS(environment.apiURL + "/ws");
      this.stompClient = over(Sock);
      this.stompClient.connect({}, this.onConnected, this.onError); 
      this.clientService.findAllRideInvitesForUser(this.loggedUser?.email as string)
      .subscribe({
        next: (data) => {
          console.log(`Data: ${data}`);
          this.rideInvites = data;
          console.log(`Ride invites: ${this.rideInvites}`);
        },
        error: (err) => {
          console.log(err);
        }
      });
    }

    onConnected = () => {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();          
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/ride-invites", this.onRideInvitesReceived);
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/remind-for-ride", this.onRideReminder);
      this.stompClient.subscribe("/topic/response-to-other-clients", (data) => this.onResponseToOtherClients(data));

      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + '/blocked-user', (message: { body: string }) => {
        let blockUserRequest: BlockUserRequest= JSON.parse(message.body);
        this.toastr.warning(`You have been blocked because: ${blockUserRequest.description}\nYou won't be able to create new rides.`, "Blocked", {timeOut:20000});
      });
    }

    onRideInvitesReceived = (payload: StompMessage) => {
      this.clientService.findAllRideInvitesForUser(this.loggedUser?.email as string)
      .subscribe({
        next: (data) => {
          console.log(`Data: ${data}`);
          this.rideInvites = data;
          console.log(`Ride invites: ${this.rideInvites}`);
        },
        error: (err) => {
          console.log(err);
        }
      });
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

    onAcceptRideInvite(rideInvite: RideInvite, index: number){
      console.log(this.rideInvites);
      this.stompClient.send('/app/ride-response', {}, JSON.stringify({
        email: rideInvite?.emailFrom,
        responderEmail: this.loggedUser?.email,
        isAccepted: true
      }));

      this.clientService.changeDriveInvitationStatus(rideInvite?.id as number, true).subscribe({
        next: (data) => {
          console.log(data);
          this.rideInvites.splice(index, 1);
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
  
    onRejectRideInvite(rideInvite: RideInvite, index: number){
      this.stompClient.send('/app/ride-response', {}, JSON.stringify({
        email: rideInvite?.emailFrom,
        responderEmail: this.loggedUser?.email,
        isAccepted: false
      }));
  
      this.clientService.changeDriveInvitationStatus(rideInvite?.id as number, false).subscribe();
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

    openDialog(rideInvite: RideInvite, index: number) {
      console.log(this.rideInvites);
      const dialogRef = this.dialog.open(RideInviteDialogComponent,{
        data:{
          from: `Invite from ${rideInvite?.emailFrom}`,
          firstLocation: `${rideInvite?.firstLocation}`,
          destination: `${rideInvite?.destination}`,
          priceToPay: `${rideInvite?.priceToPay} Tokens`,
          buttonText: {
            ok: 'Accept',
            cancel: 'Reject'
          }
        }
      });

      dialogRef.afterClosed().subscribe((confirmed: boolean) => {
        console.log(confirmed);
        if (confirmed) {
            console.log('ulazi u accept')
            this.onAcceptRideInvite(rideInvite, index);
        }
        else{
            console.log('ulazi u reject');
            this.onRejectRideInvite(rideInvite, index);
        }
      });
    }
}
