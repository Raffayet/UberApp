import { Component, ViewChild } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { MapService, MapSearchResult } from "../../services/map.service"
import { Observable, of } from 'rxjs';
import { MapComponent } from '../map/map.component';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})

export class HomePageComponent {

  @ViewChild(MapComponent)
  private mapChild!: MapComponent;
  searchForm: FormGroup;
  pickupOptions: Observable<MapSearchResult[]>;
  destinationOptions: Observable<MapSearchResult[]>;

  constructor(private mapService: MapService) {}

  ngOnInit() {
    this.searchForm = new FormGroup({
        'pickupLocation': new FormControl('', Validators.required),
        'destination': new FormControl('', Validators.required)
    });
  }

  searchPickup() : void {
    let results : MapSearchResult[];

    this.mapService.search(this.searchForm.controls['pickupLocation'].value)
    .subscribe(res => {
      results = this.mapService.convertSearchResultsToList(res);
      this.pickupOptions = of(results);
    });
  }

  searchDestination() : void {
    let results : MapSearchResult[];
    
    this.mapService.search(this.searchForm.controls['destination'].value)
    .subscribe(res => {
      results = this.mapService.convertSearchResultsToList(res);
      this.destinationOptions = of(results);
    });
  }

  pinPickupLocation(option: MapSearchResult) : void {
    this.mapChild.pinNewPickupResult(option);
  }

  pinDestinationLocation(option: MapSearchResult) : void {
    this.mapChild.pinNewDestinationResult(option);
  }

}
