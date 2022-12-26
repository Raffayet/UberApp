import { Injectable } from '@angular/core';
import { RideRequest } from 'src/app/model/RideRequest';
import { MapComponent } from '../../shared/components/map/map.component';

@Injectable({
  providedIn: 'root'
})
export class RideRequestStateService {

  rideRequest: RideRequest = {
    initiatorEmail: "",
    locations: [],
    price: 0,
    pricePerPassenger: 0,
    vehicleType: "",
    people: [],
    peopleLeftToRespond: []
  }

  mapa: MapComponent;

  constructor() { }

  reset(){
    this.rideRequest = {
      initiatorEmail: "",
      locations: [],
      price: 0,
      pricePerPassenger: 0,
      vehicleType: "",
      people: [],
      peopleLeftToRespond: []
    };
    
    this.mapa.reset();
  }
}
