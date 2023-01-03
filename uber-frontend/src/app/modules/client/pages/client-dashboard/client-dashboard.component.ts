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

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;
    navbarLabels: string[] = ["Home", "Profile", "Ride Invites", "History"];
    navbarPaths: string[] = ["request-ride-page", "profile-page", "ride-invites", "history"];
    option: string = this.navbarPaths[0];

    rideInvites: RideInvite[];
    stompClient: Client;
    loggedUser: User | null;
    
    changeOption(eventData: string): void{      
      this.router.navigate(['/client', {outlets: {'ClientRouter': [eventData]}}]);
    }

    constructor(private router: Router, private userService: UserService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private clientService: ClientService){}

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
        console.log('evo ovde')
        console.log(payloadData)     
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
          message: `Invite from ${this.rideInvites[index]?.emailFrom}\nFirst location: ${this.rideInvites[index]?.firstLocation}\nDestination: ${this.rideInvites[index]?.destination}`,
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
