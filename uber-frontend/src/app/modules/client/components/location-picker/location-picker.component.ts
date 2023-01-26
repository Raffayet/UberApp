import { MapSearchResult } from '../../services/map.service';
import { MapService } from '../../services/map.service';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from "@angular/router";
import { ClientService } from '../../services/client.service';
import { RideRequestStateService } from '../../services/ride-request-state.service';
import { CdkDragDrop, moveItemInArray } from "@angular/cdk/drag-drop";
import { Component, OnInit, ViewChild, Output, EventEmitter } from "@angular/core";
import { Observable, of, Subject } from "rxjs";
import { environment } from "src/app/environments/environment";
import { MapComponent } from 'src/app/modules/shared/components/map/map.component';
import { Point } from 'src/app/model/Point';
import { PaypalService } from 'src/app/modules/shared/services/paypal.service';
import { User } from 'src/app/model/User';

@Component({
  selector: 'app-location-picker',
  templateUrl: './location-picker.component.html',
  styleUrls: ['./location-picker.component.css']
})
export class LocationPickerComponent implements OnInit{

  @Output()
  pageNum = new EventEmitter<number>();

  destinations: MapSearchResult[] = [];

  price: number;

  pricePerPassenger: number;

  options: Observable<MapSearchResult[]>[] = [];

  loggedUser: User | null;

  constructor(private mapService: MapService, private toastr: ToastrService, private router: Router,  private paypalService: PaypalService,
    private tokenUtilsService: TokenUtilsService, private clientService: ClientService, protected stateManagement: RideRequestStateService) {}

  ngOnInit(): void {
    this.loggedUser = this.tokenUtilsService.getUserFromToken();  
  }
  
  changePageNumber(){
    this.pageNum.emit(2);
  }

  next()
  {
    if (this.stateManagement.rideRequest.locations.length < 2)
      this.toastr.warning('You must pick locations for ride!');
    else if (this.stateManagement.rideRequest.totalDistance === 0)
      this.toastr.warning('There should be distance between locations!');
    else{
      this.changePageNumber();
    }
  }

  drop(event: CdkDragDrop<MapSearchResult[]>) {
    moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    moveItemInArray(this.stateManagement.inputValues, event.previousIndex, event.currentIndex);      
    moveItemInArray(this.stateManagement.mapa.locations, event.previousIndex, event.currentIndex);  
    this.stateManagement.mapa.removePreviousRoute();
    this.automaticallyFindPath("Custom");
  }

  searchOptions(index: number) : void {  
    let results : MapSearchResult[];

    this.mapService.search(this.stateManagement.inputValues[index])
    .subscribe(res => {      
      results = this.mapService.convertSearchResultsToList(res);
      this.options[index] = of(results);
    });
  }

  pinLocation(option: MapSearchResult, index: number) : void {
    this.stateManagement.rideRequest.locations[index] = option;
    
    this.stateManagement.mapa.pinNewResult(option, index);
    this.automaticallyFindPath("Custom");
  }

  deleteLocation(index: number) : void {   
      this.stateManagement.rideRequest.locations.splice(index, 1);
      this.stateManagement.inputValues.splice(index, 1);
      this.stateManagement.mapa.deletePin(index);   
      this.automaticallyFindPath("Custom");
  }

  addLocation(index: number) : void { 
    this.stateManagement.rideRequest.locations.length < 5 ? (this.stateManagement.rideRequest.locations[index].displayName === "") ?  this.toastr.warning('Choose location for current stop!') : 
    this.stateManagement.rideRequest.locations.push({
    displayName: this.stateManagement.rideRequest.locations.length + "",
    lon: "",
    lat: ""}) : this.toastr.warning('Maximum number of stops is 5!');
  }

  getPins(){
    let markers: Array<L.Marker> = this.stateManagement.mapa.getPins();
    console.log(markers);  
    if (window.location.href === environment.frontURL) 
      this.router.navigateByUrl('login') 
  }

  automaticallyFindPath(routeType: string): void{
    this.stateManagement.mapa.reset();
    
    this.mapService.automaticallyFindPath(routeType, this.stateManagement.mapa.locations).subscribe({
      next: data => {
          let coords: Array<Point> = data.atomicPoints;
          this.stateManagement.rideRequest.totalDistance = data.distance;
          coords = coords.map(coord => new Point(coord.lat, coord.lng));
          this.stateManagement.mapa.drawRoute(coords);
      },
      error: error => {
          console.error('There was an error!', error);
      }
    });
  }

  createFavoriteRoute(): void
  {
    this.clientService.addFavoriteRoute(this.stateManagement.rideRequest.locations, this.loggedUser?.email as string)
    .subscribe({
      next: data => {
        console.log(data);
        if(String(data) === 'true')
        {
          this.toastr.success("Successfully added to favorite routes");
        }

        else{
          this.toastr.warning("Already added to favorites");
        }
      },
      error: error => {
        console.error(error);
      }
    });
  }
}
