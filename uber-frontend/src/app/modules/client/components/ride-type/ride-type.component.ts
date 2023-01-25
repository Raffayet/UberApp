import { UserService } from './../../../shared/services/user.service';
import { DriverService } from './../../../admin/services/driver.service';
import { RideRequestStateService } from '../../services/ride-request-state.service';
import { Component, OnInit, ViewChild, Output, EventEmitter } from "@angular/core";
import { MapService } from '../../services/map.service';
import { Point } from 'src/app/model/Point';
import { ToastrService } from 'ngx-toastr';
import { catchError, throwError } from 'rxjs';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ride-type',
  templateUrl: './ride-type.component.html',
  styleUrls: ['./ride-type.component.css']
})
export class RideTypeComponent implements OnInit{

  @Output()
  pageNum = new EventEmitter<number>();
  
  vehicleTypes: string[] = [];

  routeTypes: string[] = ['Custom', 'Optimal', 'Alternative'];

  currentAmount: number;

  constructor(private mapService: MapService, protected stateManagement: RideRequestStateService, private toastr: ToastrService, private driverService:DriverService, 
              private userService:UserService, protected tokenUtilsService: TokenUtilsService, private router:Router){}
  
  
  ngOnInit(): void {
    this.loadVehicleTypeOptions();
  }

  loadVehicleTypeOptions(){
    this.driverService.getVehicleTypes()
        .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
        .subscribe({
          next: (res:string[]) => {
            this.vehicleTypes = res;
          },
          error: (err) => {
            this.vehicleTypes = []
            console.log("Greska")
          },
        });
  }

  next(){
    const loggedUser = this.tokenUtilsService.getUserFromToken();
    this.userService.checkIfUserIsBlocked(loggedUser?.email).subscribe({
      next: (data:boolean) => {
          if(data){
            this.toastr.warning("You can't request a new ride because you are blocked!", "Blocked");
            this.stateManagement.reset();
            this.goBack();
          }
          else{
            if(this.currentAmount < this.stateManagement.rideRequest.pricePerPassenger)
                this.toastr.warning('You do not have enough tokens for ride!')
            this.changePageNumber();
          }

      },
      error: error => {
          console.error('There was an error!', error);
      }
    });;

  }

  goBack(){
    this.pageNum.emit(1);
  }

  goToLogin(){
    this.router.navigateByUrl("/login");

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
          let coords: Array<Point> = data.atomicPoints;
          coords = coords.map(coord => new Point(coord.lat, coord.lng));
          this.stateManagement.mapa.drawRoute(coords)
      },
      error: error => {
          console.error('There was an error!', error);
      }
    });
  }

}
