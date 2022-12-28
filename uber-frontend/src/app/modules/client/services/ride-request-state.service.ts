import { Injectable } from '@angular/core';
import { RideRequest } from 'src/app/model/RideRequest';
import { MapComponent } from '../../shared/components/map/map.component';

@Injectable({
  providedIn: 'root'
})
export class RideRequestStateService {

  rideRequest: RideRequest = {
    initiatorEmail: "",

    locations: [{
      displayName: "",
      lon: "",
      lat: ""}],

    price: 0,
    pricePerPassenger: 0,
    vehicleType: "",
    routeType: "Custom",
    people: [],
    peopleLeftToRespond: [],
    totalDistance: 0,
    invitesSent: false
  }

  inputValues: string[] = [];

  mapa: MapComponent;

  constructor() { }

  reset(){
    this.rideRequest = {
      initiatorEmail: "",

      locations: [{
        displayName: "",
        lon: "",
        lat: ""}],
        
      price: 0,
      pricePerPassenger: 0,
      vehicleType: "",
      routeType: "Custom",
      people: [],
      peopleLeftToRespond: [],
      totalDistance: 0,
      invitesSent: false
    };
    
    this.inputValues = [];
    this.mapa.reset();
  }
}
