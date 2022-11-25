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

  private pickupLocation : L.Marker;
  private destinationLocation : L.Marker;
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

  constructor() { }

  ngAfterViewInit(): void { 
    this.initMap();

    //pickup and destination markers initialization
    this.pickupLocation = L.marker([0, 0], {icon: this.customIcon});
    this.destinationLocation = L.marker([0, 0], {icon: this.customIcon});

    this.featureGroup = new L.FeatureGroup([ this.pickupLocation , this.destinationLocation]);
  }

  pinNewPickupResult(pin: MapSearchResult): void {    
    var newLatLng = new L.LatLng(parseFloat(pin.lat), parseFloat(pin.lon));

    this.pickupLocation.setLatLng(newLatLng); 
    this.pickupLocation.addTo(this.map);

    this.map.setView(newLatLng, 12);

    this.map.fitBounds(this.featureGroup.getBounds());
  }

  pinNewDestinationResult(pin: MapSearchResult): void {
    var newLatLng = new L.LatLng(parseFloat(pin.lat), parseFloat(pin.lon));

    this.destinationLocation.setLatLng(newLatLng); 
    this.destinationLocation.addTo(this.map);

    this.map.setView(newLatLng, 12);

    this.map.fitBounds(this.featureGroup.getBounds());
  }
  
}
