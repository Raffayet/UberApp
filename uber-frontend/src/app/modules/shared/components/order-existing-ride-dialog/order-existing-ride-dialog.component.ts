import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import * as moment from 'moment';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/app/environments/environment';
import { MapSearchResult } from 'src/app/model/MapSearchResult';
import { Point } from 'src/app/model/Point';
import { Ride } from 'src/app/model/Ride';
import { User } from 'src/app/model/User';
import { ClientService } from 'src/app/modules/client/services/client.service';
import { MapService } from 'src/app/modules/client/services/map.service';
import { RideRequestStateService } from 'src/app/modules/client/services/ride-request-state.service';
import { TokenUtilsService } from '../../services/token-utils.service';
import { over, Client, Message as StompMessage} from 'stompjs';
import { ResponseToIniciator } from 'src/app/model/ResponseToIniciator';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-order-existing-ride-dialog',
  templateUrl: './order-existing-ride-dialog.component.html',
  styleUrls: ['./order-existing-ride-dialog.component.css']
})
export class OrderExistingRideDialogComponent {

  locations: MapSearchResult[];
  maxReserveTime: Date;
  minReserveTime = moment().toDate();
  currentTime = Date.now();
  loggedUser: User | null;
  ride: Ride;
  private stompClient : Client;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<OrderExistingRideDialogComponent>, private mapService: MapService, protected rideRequestStateService: RideRequestStateService, private clientService: ClientService, private tokenUtilsService: TokenUtilsService, private toastr: ToastrService){

      if(data)
      {
        this.locations = data.locations;
        this.ride = data.ride;
        this.rideRequestStateService.mapa.pinNewResult(this.locations[0], 0);
        this.rideRequestStateService.mapa.pinNewResult(this.locations[1], 1);
        
        if(this.locations.length === 3)
        {
          this.rideRequestStateService.mapa.pinNewResult(this.locations[2], 2);
        }

        else if(this.locations.length === 4)
        {
          this.rideRequestStateService.mapa.pinNewResult(this.locations[3], 3);
        }

        else if(this.locations.length === 5)
        {
          this.rideRequestStateService.mapa.pinNewResult(this.locations[4], 4);
        }

        this.automaticallyFindPath('Custom');
      }
    }

    ngOnInit() { 
      this.loggedUser = this.tokenUtilsService.getUserFromToken();   
      
      let Sock = new SockJS(environment.apiURL + "/ws");
      this.stompClient = over(Sock);
      this.stompClient.connect({}, this.onConnected, () => {});  
    }

    onConnected = () => {
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/response-ride-invites", (data) => this.onRideInviteResponseReceived(data));
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/response-to-client", (data) => this.onResponseToClient(data));
      this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/invited-person-not-have-tokens", (data) => this.notEnoughTokens(data));
    }

    onRideInviteResponseReceived(payload: StompMessage){
      let dto = JSON.parse(payload.body);
      
      console.log(dto);
      const index = this.rideRequestStateService.rideRequest.people.indexOf(dto.responderEmail);
  
      let isAccepted = (dto.isAccepted === 'true');
  
      if(isAccepted)
      {
        this.toastr.success(`${dto.responderEmail} has accepted your ride invite`);
      }
      else
      {
        this.toastr.error(`${dto.responderEmail} has rejected your ride invite`);
      }
      console.log(index);
      if (index >= 0) {
        if(!isAccepted){
          this.rideRequestStateService.rideRequest.people.splice(index, 1);
          this.rideRequestStateService.rideRequest.pricePerPassenger = this.rideRequestStateService.rideRequest.price / (this.rideRequestStateService.rideRequest.people.length + 1);  
        }
        this.rideRequestStateService.rideRequest.peopleLeftToRespond.splice(index, 1);
      }
    }

    notEnoughTokens = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      let responseToIniciator: ResponseToIniciator;
      responseToIniciator = payloadData;
      this.toastr.error(responseToIniciator.messageContent);
    }

    onResponseToClient = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      let responseToClient: ResponseToIniciator;
      responseToClient = payloadData;
  
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

    onCloseClick(): void {
      this.rideRequestStateService.reset();
      this.dialogRef.close(false);
    }

    automaticallyFindPath(routeType: string): void{
      this.mapService.automaticallyFindPathForHistory(routeType, this.locations).subscribe({
        next: data => {
            console.log(data);
            let coords: Array<Point> = data.atomicPoints;
            coords = coords.map(coord => new Point(coord.lat, coord.lng));
            this.rideRequestStateService.mapa.drawRoute(coords);
            this.rideRequestStateService.rideRequest.totalDistance = data.distance / 1000;
        },
        error: error => {
            console.error('There was an error!', error);
        }
      });
    }

  orderRide()
  {
    this.fillRideRequest();
    this.rideRequestStateService.rideRequest.initiatorEmail = this.loggedUser?.email as string;

    if(this.rideRequestStateService.rideRequest.isReserved)
    {
      this.submitReservation();
    }

    else{
      this.submitClassicRequest();
    }
  }

  fillRideRequest(){
    this.rideRequestStateService.rideRequest.invitesSent = true;
    this.rideRequestStateService.rideRequest.invitesSent = true;
    this.rideRequestStateService.rideRequest.locations = this.locations;
    this.fillRideRequestWithPeople();
    this.rideRequestStateService.rideRequest.peopleLeftToRespond = [];
    this.rideRequestStateService.rideRequest.price = this.ride.price;
    this.rideRequestStateService.rideRequest.pricePerPassenger = this.ride.pricePerPassenger;
    this.rideRequestStateService.rideRequest.routeType = this.ride.routeType;
    this.rideRequestStateService.rideRequest.timeOfRequestForReservation = new Date();
    this.rideRequestStateService.rideRequest.vehicleType = this.ride.vehicleType;
    console.log(this.rideRequestStateService.rideRequest);
  }

  fillRideRequestWithPeople()
  {
    for(let user of this.ride.clients)
    {
      this.rideRequestStateService.rideRequest.people.concat(user.email);
    }
  }

  submitClassicRequest():void
  {
    this.clientService.submitRequest(this.rideRequestStateService.rideRequest)
      .subscribe({
        next: data => {
          console.log(data);
        },
        error: error => {
          console.error(error);
        }
      });
      
    this.rideRequestStateService.reset();
  }

  submitReservation(): void {    
    this.rideRequestStateService.rideRequest.initiatorEmail = this.loggedUser?.email as string;

    this.clientService.submitReservation(this.rideRequestStateService.rideRequest)
      .subscribe({
        next: data => {
          console.log(data);
        },
        error: error => {
          console.error(error);
        }
      });
      
    this.rideRequestStateService.reset();
  }

  changeTime(): void{
    this.minReserveTime = new Date();
    this.maxReserveTime = new Date();
    this.maxReserveTime.setHours(this.maxReserveTime.getHours() + 5);
    this.rideRequestStateService.rideRequest.isReserved = true;
  }
}
