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

  routeTypes: string[] = ['Custom', 'Optimal', 'Alternative'];

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

  calculatePrice(vType: string): void{
    this.stateManagement.rideRequest.vehicleType = vType;
    this.mapService.calculatePrice(this.stateManagement.rideRequest.vehicleType, this.stateManagement.rideRequest.totalDistance).subscribe({
      next: data => {
          this.stateManagement.rideRequest.price = data;
          this.stateManagement.rideRequest.pricePerPassenger = this.stateManagement.rideRequest.price;
      },
      error: error => {
          console.error('There was an error!', error);
      }
    });
  }

  automaticallyFindPath(routeType: string): void{
    this.mapService.automaticallyFindPath(routeType, this.stateManagement.mapa.locations).subscribe({
      next: data => {
          this.stateManagement.rideRequest.totalDistance = data.distance;
          this.calculatePrice(this.stateManagement.rideRequest.vehicleType);
          let coords: Array<Point> = data.points;
          coords = coords.map(coord => new Point(coord.lng, coord.lat));
          this.stateManagement.mapa.drawRoute(coords)
      },
      error: error => {
          console.error('There was an error!', error);
      }
    });
  }

}
