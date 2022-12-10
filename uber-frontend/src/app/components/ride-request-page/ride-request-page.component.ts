import { Component, Input, ViewChild } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { MapService, MapSearchResult } from "../../services/map.service"
import { Observable, of } from 'rxjs';
import { MapComponent } from '../map/map.component';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import { ToastrService } from 'ngx-toastr';
import { environment } from './../../environments/environment'
import { Router } from '@angular/router';
import { trigger, transition, animate, style } from '@angular/animations'
import {MatChipEditedEvent, MatChipInputEvent} from '@angular/material/chips';
import { Person } from 'src/app/model/Person';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import { PaypalService } from 'src/app/services/paypal.service';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/services/token-utils.service';

@Component({
  selector: 'ride-request-page',
  templateUrl: './ride-request-page.component.html',
  styleUrls: ['./ride-request-page.component.css'],
  animations: [
    trigger('firstWindowAnimation', [
      transition(':enter', [
        style({transform: 'translateX(-100%)'}),
        animate('300ms ease-in', style({transform: 'translateX(0%)'}))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({transform: 'translateX(-100%)'}))
      ])
    ]),
    trigger('secondWindowAnimation', [
      transition(':enter', [
        style({transform: 'translateX(100%)'}),
        animate('300ms ease-in', style({transform: 'translateX(0%)'}))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({transform: 'translateX(100%)'}))
      ])
    ])
  ]
})

export class RideRequestPageComponent {

  @ViewChild(MapComponent)
  private mapChild!: MapComponent;

  loggedUser: User | null

  currentAmount: number

  destinations: MapSearchResult[] = [];
  options: Observable<MapSearchResult[]>[] = [];
  inputValues: string[] = [];

  vehicleType: string;
  vehicleTypes: string[] = ['Regular', 'Baby Seats', 'Pet Seats'];

  price: Number

  firstWindow: boolean = true

  secondWindow: boolean = false

  animationsStarted: boolean = false

  //add more people
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  people: Person[] = [];
  
  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.people.push({name: value});
    }

    event.chipInput!.clear();
  }

  remove(person: Person): void {
    const index = this.people.indexOf(person);

    if (index >= 0) {
      this.people.splice(index, 1);
    }
  }

  edit(person: Person, event: MatChipEditedEvent) {
    const value = event.value.trim();

    if (!value) {
      this.remove(person);
      return;
    }

    const index = this.people.indexOf(person);
    if (index > 0) {
      this.people[index].name = value;
    }
  }

  constructor(private mapService: MapService, private toastr: ToastrService, private router: Router, private paypalService: PaypalService, private tokenUtilsService: TokenUtilsService) {}

  trigger()
  {
    this.firstWindow = false
    this.secondWindow = true
  }

  triggerBack()
  {
    this.firstWindow = true
    this.secondWindow = false
  }

  drop(event: CdkDragDrop<MapSearchResult[]>) {
    moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    moveItemInArray(this.inputValues, event.previousIndex, event.currentIndex);      
    moveItemInArray(this.mapChild.locations, event.previousIndex, event.currentIndex);  
    this.mapChild.removePreviousRoute()
    this.mapChild.createRoute()   
  }

  ngOnInit() { 
    this.loggedUser = this.tokenUtilsService.getUserFromToken();  
    this.getAmountOfTokens() 
    console.log(this.loggedUser)
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

  getAmountOfTokens(){
    this.paypalService.getAmountOfTokens(this.loggedUser?.email as string).subscribe(
      (data: number) => this.currentAmount = data
    );
  }
}
