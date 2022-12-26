import { RideRequestStateService } from '../../services/ride-request-state.service';
import { Component, OnInit, ViewChild, Output, EventEmitter } from "@angular/core";
import { MapService } from '../../services/map.service';
import { Point } from 'src/app/model/Point';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-ride-type',
  templateUrl: './ride-type.component.html',
  styleUrls: ['./ride-type.component.css']
})
export class RideTypeComponent {

  @Output()
  pageNum = new EventEmitter<number>();
  
  vehicleTypes: string[] = ['Regular', 'Baby Seats', 'Pet Seats'];
  vehicleType: string;

  routeTypes: string[] = ['Custom', 'Optimal', 'Alternative'];
  routeType: string = this.routeTypes[0];

  currentAmount: number;

  constructor(private mapService: MapService, protected stateManagement: RideRequestStateService, private toastr: ToastrService){}

  next(){
    if(this.currentAmount < this.stateManagement.rideRequest.pricePerPassenger)
        this.toastr.warning('You do not have enough tokens for ride!')
    this.changePageNumber();
  }

  changePageNumber(){
    this.pageNum.emit(3);
  }


  changeRouteType(): void{
    switch(this.routeType){
      case "Custom":
        this.createRoute();
        break;
      case "Optimal":
        this.automaticallyFindPath(true);
        break;
      case "Alternative":
        this.automaticallyFindPath(false);
        break;
    }
  }

  createRoute(): void{
    this.stateManagement.mapa.createRoute();
  } 

  calculatePrice(vType: string): void{
    this.stateManagement.rideRequest.vehicleType = vType;
    this.stateManagement.rideRequest.price = this.mapService.calculatePrice(this.stateManagement.rideRequest.vehicleType, this.stateManagement.mapa.locations)
    this.stateManagement.rideRequest.pricePerPassenger = this.stateManagement.rideRequest.price;
  }

  automaticallyFindPath(isBest: boolean): void{
    this.mapService.automaticallyFindPath(isBest, this.stateManagement.mapa.locations).subscribe({
      next: data => {
          let coords: Array<Point> = data;
          coords = coords.map(coord => new Point(coord.lng, coord.lat));
          this.stateManagement.mapa.drawRoute(coords)
      },
      error: error => {
          console.error('There was an error!', error);
      }
    });
  }

}
