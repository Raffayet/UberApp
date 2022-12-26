import { MapSearchResult } from '../../services/map.service';
import { MapService } from '../../services/map.service';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from "@angular/router";
import { ClientService } from '../../services/client.service';
import { RideRequestStateService } from '../../services/ride-request-state.service';
import { CdkDragDrop, moveItemInArray } from "@angular/cdk/drag-drop";
import { Component, OnInit, ViewChild, Output, EventEmitter } from "@angular/core";
import { Observable, of } from "rxjs";
import { environment } from "src/app/environments/environment";
import { MapComponent } from 'src/app/modules/shared/components/map/map.component';
import { Point } from 'src/app/model/Point';
import { User } from 'src/app/model/User';
import { PaypalService } from 'src/app/modules/shared/services/paypal.service';

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

  inputValues: string[] = [];

  options: Observable<MapSearchResult[]>[] = [];

  totalDistance: number;

  constructor(private mapService: MapService, private toastr: ToastrService, private router: Router,  private paypalService: PaypalService,
    private tokenUtilsService: TokenUtilsService, private clientService: ClientService, protected stateManagement: RideRequestStateService) {}

  ngOnInit(): void {

    this.destinations.push({
      displayName: "",
      lon: "",
      lat: ""});
  }

  changePageNumber(){
    this.pageNum.emit(2);
  }

  next()
  {
    this.getTotalDistance();

    if (this.destinations.length < 2)
      this.toastr.warning('You must pick locations for ride!');
    else if (this.totalDistance === 0)
      this.toastr.warning('There should be distance between locations!');
    else{
      this.stateManagement.rideRequest.locations = [...this.destinations];
      this.changePageNumber();
    }
  }

  drop(event: CdkDragDrop<MapSearchResult[]>) {
    moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    moveItemInArray(this.inputValues, event.previousIndex, event.currentIndex);      
    moveItemInArray(this.stateManagement.mapa.locations, event.previousIndex, event.currentIndex);  
    this.stateManagement.mapa.removePreviousRoute();
    this.stateManagement.mapa.createRoute();
    this.stateManagement.rideRequest.price = this.mapService.calculatePrice(this.stateManagement.rideRequest.vehicleType, this.stateManagement.mapa.locations);
    this.stateManagement.rideRequest.pricePerPassenger = this.stateManagement.rideRequest.price;
  }

  
  searchOptions(index: number) : void {    
    let results : MapSearchResult[];

    this.mapService.search(this.inputValues[index])
    .subscribe(res => {      
      results = this.mapService.convertSearchResultsToList(res);
      this.options[index] = of(results);
    });
  }

  pinLocation(option: MapSearchResult, index: number) : void {
    this.destinations[index] = option;
    
    this.stateManagement.mapa.pinNewResult(option, index);
    this.stateManagement.rideRequest.price = this.mapService.calculatePrice(this.stateManagement.rideRequest.vehicleType, this.stateManagement.mapa.locations);
    this.stateManagement.rideRequest.pricePerPassenger = this.stateManagement.rideRequest.price;
  }

  deleteLocation(index: number) : void {      
      this.destinations.splice(index, 1);
      this.inputValues.splice(index, 1);
      this.stateManagement.mapa.deletePin(index);    
      this.stateManagement.rideRequest.price = this.mapService.calculatePrice(this.stateManagement.rideRequest.vehicleType, this.stateManagement.mapa.locations);
      this.stateManagement.rideRequest.pricePerPassenger = this.stateManagement.rideRequest.price;
  }

  addLocation(index: number) : void {    
    this.destinations.length < 5 ? (this.destinations[index].displayName === "") ?  this.toastr.warning('Choose location for current stop!') : 
    this.destinations.push({
      displayName: this.destinations.length + "",
      lon: "",
      lat: ""}) : this.toastr.warning('Maximum number of stops is 5!');
  }

  getPins(){
    let markers: Array<L.Marker> = this.stateManagement.mapa.getPins();
    console.log(markers);  
    if (window.location.href === environment.frontURL) 
      this.router.navigateByUrl('login') 
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
  
  getTotalDistance(): void{
    this.totalDistance = this.mapService.getTotalDistance(this.stateManagement.mapa.locations);
  }
}
