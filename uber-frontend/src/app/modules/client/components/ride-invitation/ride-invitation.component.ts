import { RideRequestStateService } from './../../services/ride-request-state.service';
import { Component, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { MatChipInputEvent, MatChipEditedEvent } from "@angular/material/chips";
import { Observable, of } from "rxjs";
import { environment } from "src/app/environments/environment";
import { Point } from "src/app/model/Point";
import { ENTER, COMMA } from "@angular/cdk/keycodes";
import { User } from "src/app/model/User";
import { MapComponent } from "src/app/modules/shared/components/map/map.component";
import { TokenUtilsService } from "src/app/modules/shared/services/token-utils.service";
import { ClientService } from "../../services/client.service";
import { MapSearchResult, MapService } from "../../services/map.service";
import { PaypalService } from 'src/app/modules/shared/services/paypal.service';
import * as SockJS from 'sockjs-client';
import { over, Client, Message as StompMessage} from 'stompjs';

@Component({
  selector: 'app-ride-invitation',
  templateUrl: './ride-invitation.component.html',
  styleUrls: ['./ride-invitation.component.css']
})
export class RideInvitationComponent implements OnInit{

  loggedUser: User | null;

  animationsStarted: boolean = false;

  maxPeoplePerDrive = environment.maxPeoplePerDrive;

  private stompClient : Client;

  currentAmount: number;

  //add more people
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;

  constructor(private mapService: MapService, private toastr: ToastrService, private router: Router, private paypalService: PaypalService,
    private tokenUtilsService: TokenUtilsService, private clientService: ClientService, protected stateManagement: RideRequestStateService) {}


    
  ngOnInit() { 
    this.loggedUser = this.tokenUtilsService.getUserFromToken();  
    this.getAmountOfTokens();
      
    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, () => {});    
  }

  onConnected = () => {
    this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/response-ride-invites", (data) => this.onRideInviteResponseReceived(data));
  }

  getAmountOfTokens(){
    this.paypalService.getAmountOfTokens(this.loggedUser?.email as string).subscribe(      
      (data: number) => this.currentAmount = data
    );
  }

  onRideInviteResponseReceived(payload: StompMessage){
    let dto = JSON.parse(payload.body);
    
    const index = this.stateManagement.rideRequest.people.indexOf(dto.responderEmail);
    let isAccepted = Boolean(dto.isAccepted);
    
    
    if (index >= 0) {
      if(!isAccepted){
        this.stateManagement.rideRequest.people.splice(index, 1);
      }
      this.stateManagement.rideRequest.peopleLeftToRespond.splice(index, 1);  
    }
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      
      this.stateManagement.rideRequest.people.push(value);
      this.stateManagement.rideRequest.peopleLeftToRespond.push(value);
    }
    event.chipInput!.clear();
  }

  remove(person: string): void {
    const index = this.stateManagement.rideRequest.people.indexOf(person);

    if (index >= 0) {
      this.stateManagement.rideRequest.people.splice(index, 1);
      this.stateManagement.rideRequest.peopleLeftToRespond.splice(index, 1);
    }
  }

  edit(person: string, event: MatChipEditedEvent) {
    const value = event.value.trim();

    if (!value) {
      this.remove(person);
      return;
    }

    const index = this.stateManagement.rideRequest.people.indexOf(person);
    if (index > 0) {
      this.stateManagement.rideRequest.people[index] = value;
      this.stateManagement.rideRequest.peopleLeftToRespond[index] = value;
    }
  }

  
  splitFare(): void{
    this.stateManagement.rideRequest.invitesSent = true
    this.stateManagement.rideRequest.pricePerPassenger = this.stateManagement.rideRequest.price / (this.stateManagement.rideRequest.people.length + 1)    //+ 1 se odnosi i na coveka koji je rezervisao voznju
    this.createDriveInvitation(true);
  }

  onYourCharge(): void{
    this.stateManagement.rideRequest.invitesSent = true
    this.createDriveInvitation(false);
  }

  createDriveInvitation(isSplitFare: boolean){
    let priceToPay = isSplitFare ? this.stateManagement.rideRequest.pricePerPassenger: 0;
    this.clientService.createDriveInvitation(this.loggedUser, this.stateManagement.rideRequest.people, this.stateManagement.rideRequest.locations, priceToPay, this.stompClient)
      .subscribe({
        next: data => {
          console.log(data)
        },
        error: error => {
          console.error(error.ok);
        }
      });
  }
  
  submitRequest(): void {    
    this.stateManagement.rideRequest.initiatorEmail = this.loggedUser?.email as string;
    this.clientService.submitRequest(this.stateManagement.rideRequest)
      .subscribe({
        next: data => {
          console.log(data)
        },
        error: error => {
          console.error(error.ok);
        }
      });
      
    this.stateManagement.reset();
  }

}
