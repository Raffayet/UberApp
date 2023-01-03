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
    this.ridesToTake.push(payloadData);
  }

  openDialog(index: number) {
    const dialogRef = this.dialog.open(RideToTakeDialogComponent,{
      data:{
        message: `Ride request from ${this.ridesToTake[index]?.initiatorEmail}\nFirst location: ${this.ridesToTake[index]?.firstLocation}\nDestination: ${this.ridesToTake[index]?.destination}`,
        buttonText: {
          accept: 'Accept',
          reject: 'Reject'
        }
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (confirmed) {
          this.onAcceptRideToTake(index);
      }

      else{
          this.onRejectRideToTake(index);
      }
    });
  }

  onAcceptRideToTake(index: number) {
    this.driverService.assignDriveToDriver(this.loggedUser?.email as string, this.ridesToTake.at(index)?.requestId as number, this.ridesToTake.at(index)?.initiatorEmail as string).subscribe();
    this.ridesToTake.splice(index, 1);
  }
  
  onRejectRideToTake(index: number) {
    this.ridesToTake.splice(index, 1);
  }

}
