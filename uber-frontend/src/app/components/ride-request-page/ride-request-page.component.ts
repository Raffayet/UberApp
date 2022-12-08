import { Component, ViewChild } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { MapService, MapSearchResult } from "../../services/map.service"
import { Observable, of } from 'rxjs';
import { MapComponent } from '../map/map.component';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import { ToastrService } from 'ngx-toastr';
import { environment } from './../../environments/environment'
import { Router } from '@angular/router';
import {MatRadioModule} from '@angular/material/radio';

@Component({
  selector: 'ride-request-page',
  templateUrl: './ride-request-page.component.html',
  styleUrls: ['./ride-request-page.component.css'],
})

export class RideRequestPageComponent {

  @ViewChild(MapComponent)
  private mapChild!: MapComponent;

  destinations: MapSearchResult[] = [];
  options: Observable<MapSearchResult[]>[] = [];
  inputValues: string[] = [];

  vehicleType: string;
  vehicleTypes: string[] = ['Regular', 'Baby Seats', 'Pet Seats'];

  price: Number

  constructor(private mapService: MapService, private toastr: ToastrService, private router: Router) {}

  drop(event: CdkDragDrop<MapSearchResult[]>) {
    moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    moveItemInArray(this.inputValues, event.previousIndex, event.currentIndex);      
    moveItemInArray(this.mapChild.locations, event.previousIndex, event.currentIndex);  
    this.mapChild.removePreviousRoute()
    this.mapChild.createRoute()   
  }

  ngOnInit() {    
    this.destinations.push({
      displayName: "",
      lon: "",
      lat: ""});
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
    this.mapChild.pinNewResult(option, index);
    this.price = this.mapService.calculatePrice(this.vehicleType, this.mapChild.locations)
  }

  deleteLocation(index: number) : void {      
      this.destinations.splice(index, 1);
      this.inputValues.splice(index, 1);
      this.mapChild.deletePin(index);    
      this.price = this.mapService.calculatePrice(this.vehicleType, this.mapChild.locations)      
  }

  addLocation(index: number) : void {    
    this.destinations.length < 5 ? (this.destinations[index].displayName === "") ?  this.toastr.warning('Choose location for current stop!') : 
    this.destinations.push({
      displayName: this.destinations.length + "",
      lon: "",
      lat: ""}) : this.toastr.warning('Maximum number of stops is 5!');
  }

  getPins(){
    let markers: Array<L.Marker> = this.mapChild.getPins();
    console.log(markers);  
    if (window.location.href === environment.frontURL) 
      this.router.navigateByUrl('login') 
  }

  createRoute(): void{
    this.mapChild.createRoute()
  } 

  calculatePrice(vType: string): void{
    this.vehicleType = vType
    this.price = this.mapService.calculatePrice(this.vehicleType, this.mapChild.locations)
  }
}
