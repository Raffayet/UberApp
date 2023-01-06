import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/app/environments/environment';
import { Ride } from 'src/app/model/Ride';
import { RideToShow } from 'src/app/model/RideToShow';
import { User } from 'src/app/model/User';
import { ClientService } from 'src/app/modules/client/services/client.service';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { Client, over } from 'stompjs';
import { RejectionDialogComponent } from '../../components/rejection-dialog/rejection-dialog.component';
import { DriverService } from '../../services/driver.service';

@Component({
  selector: 'app-rides-to-do',
  templateUrl: './rides-to-do.component.html',
  styleUrls: ['./rides-to-do.component.css']
})
export class RidesToDoComponent {

  loggedDriver: User | null;
  ridesToDo: RideToShow[];
  stompClient: Client;

  constructor(private driverService: DriverService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private clientService: ClientService) {}

  ngOnInit() {
    this.loggedDriver = this.tokenUtilsService.getUserFromToken(); 

    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);
  }

  onConnected = () => {
    this.driverService.getDriversRides(this.loggedDriver?.email as string)
          .subscribe(val => this.ridesToDo = val); 
  }

  onError = () => {
    console.log("Socket error.");    
  }

  refundTokens(index: number, ridesToDo: RideToShow[])
  {
    this.clientService.refundTokensAfterAccepting(ridesToDo.at(index)?.id as number).subscribe({
      next: (data) => {
        console.log(data);
      },
      error: (error) => {
        console.log(error);
      }
    });
    ridesToDo.splice(index, 1);
  }

  rejectDrive(index: number, notifications: RideToShow[], reasonForRejection: string)
  {
    this.driverService.rejectDriveAfterAccepting(this.loggedDriver?.email as string, notifications.at(index)?.id as number, notifications.at(index)?.initiator as string, reasonForRejection).subscribe({
      next: data => {
        console.log(data);
      },
      error: error => {
        console.error(error);
      }
    });
  }

  openRejectionDialog(index: number, ridesToDo: RideToShow[]) {
    const rejectionDialogRef = this.dialog.open(RejectionDialogComponent);
    rejectionDialogRef.componentInstance.reasonForRejectionEmitter.subscribe((reasonForRejection: string) => {
      // If the input value is not empty, close the dialog
      if (reasonForRejection !== '' && reasonForRejection !== undefined) {
        rejectionDialogRef.close();
        this.rejectDrive(index, ridesToDo, reasonForRejection);
        this.refundTokens(index, ridesToDo);
      }
    });
  }

  cancelRide(index: number){
    this.openRejectionDialog(index, this.ridesToDo);
  }
}
