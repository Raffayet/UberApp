import * as L from 'leaflet';
import { AfterViewInit, Component } from '@angular/core';
import { MapSearchResult } from "../../services/map.service"

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit {

  private map: L.Map;
  private customIcon : L.Icon;

  locations : Array<L.Marker> = new Array<L.Marker>();
  private featureGroup: L.FeatureGroup;

  private initMap(): void {

    this.customIcon = L.icon({
      iconUrl: 'https://www.freeiconspng.com/thumbs/pin-png/pin-png-28.png',
      iconSize: [30, 40],
      iconAnchor: [15, 20],
      popupAnchor: [-3, -76]
    })

    this.map = L.map('map', {
      center: [ 45.26, 19.83 ],
      zoom: 10
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
  }

  constructor() {}

  ngAfterViewInit(): void {
    this.initMap();
    
    for(let i=0; i < 5; i++){
      this.locations.push(L.marker([0, 0], {icon: this.customIcon}));
    }        
  }
  
  pinNewResult(pin: MapSearchResult, i: number): void {  
    let newLatLng = new L.LatLng(parseFloat(pin.lat), parseFloat(pin.lon));
    
    this.locations[i].setLatLng(newLatLng);
    this.locations[i].addTo(this.map);

    this.map.setView(newLatLng, 12);    
    this.featureGroup = L.featureGroup(this.locations.filter((location) => { return location.getLatLng().lat != 0 && location.getLatLng().lng != 0; }));
    
    this.map.fitBounds(this.featureGroup.getBounds());
  }

  getPins(): Array<L.Marker>{
    return this.locations;
  }

  deletePin(index: number): void{
    this.locations[index].removeFrom(this.map);
    this.locations.splice(index, 1);
    this.locations.push(L.marker([0, 0], {icon: this.customIcon}));
    
  }

}
