import { Component, OnInit } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { environment } from "../../../../environments/environment";
import { over, Client, Message as StompMessage} from 'stompjs';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { RideToTake } from 'src/app/model/RideToTake';
import { MatDialog } from '@angular/material/dialog';
import { RideToTakeDialogComponent } from '../ride-to-take-dialog/ride-to-take-dialog.component';
import { DriverService } from '../../services/driver.service';

@Component({
  selector: 'app-driver-notification',
  templateUrl: './driver-notification.component.html',
  styleUrls: ['./driver-notification.component.css']
})
export class DriverNotificationComponent implements OnInit{

  private stompClient : Client;
  loggedUser: User | null;
  ridesToTake: RideToTake[] = [];
  reservedRidesToTake: RideToTake[] = [];

  constructor(private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private driverService: DriverService){}

  ngOnInit() {
    this.loggedUser = this.tokenUtilsService.getUserFromToken();

    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);    
  }

  onConnected = () => {
    this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/driver-notification", this.onNotificationReceived);
    // this.driverService.findAllRidesToTake(this.loggedUser?.email as string)
    //       .subscribe(val => this.ridesToTake = val);  
  }

  onError = () => {
    console.log("Socket error.");    
  }

  onNotificationReceived = (payload: StompMessage) => {
    let payloadData = JSON.parse(payload.body);
    console.log(payloadData); 
    if(payloadData.isReserved)
      this.reservedRidesToTake.push(payloadData);
    else
      this.ridesToTake.push(payloadData);
  }

  openDialog(index: number) {
    let notifications = this.reservedRidesToTake.length === 0 ? this.ridesToTake : this.reservedRidesToTake;
    const dialogRef = this.dialog.open(RideToTakeDialogComponent,{
      data:{
        from: `Ride request from ${this.ridesToTake[index]?.initiatorEmail}`,
        firstLocation: `${this.ridesToTake[index]?.firstLocation}`,
        destination: `${this.ridesToTake[index]?.destination}`,
        drivingTime:  `${this.ridesToTake[index]?.drivingTime}`,
        buttonText: {
          ok: 'Accept',
          cancel: 'Reject'
        }
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
          this.onAcceptRideToTake(0);
      }
      else{
          this.onRejectRideToTake(0);
      }
    });
  }

  onAcceptRideToTake(index: number): void {
    let notifications = this.reservedRidesToTake.length === 0 ? this.ridesToTake : this.reservedRidesToTake;
    this.driverService.assignDriveToDriver(this.loggedUser?.email as string, notifications.at(index)?.requestId as number, notifications.at(index)?.initiatorEmail as string).subscribe({
      next: data => {
        console.log(data);
      },
      error: error => {
        console.error(error);
      }
    });

    notifications.splice(index, 1);
  }
  
  onRejectRideToTake(index: number) {
    let notifications = this.reservedRidesToTake.length === 0 ? this.ridesToTake : this.reservedRidesToTake;
    notifications.splice(index, 1);
  }
}
