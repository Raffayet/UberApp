import { MapRide } from './../../../../model/MapRide';
import * as L from 'leaflet';
import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MapSearchResult } from 'src/app/modules/client/services/map.service';
import { MapService } from 'src/app/modules/client/services/map.service';
import 'leaflet-routing-machine';
import { connect } from 'net';
import { TitleStrategy } from '@angular/router';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { environment } from 'src/app/environments/environment';
import { MapDriver } from 'src/app/model/MapRide';
import { geoJSON, icon, LayerGroup, marker } from 'leaflet';
import { RideRequestStateService } from 'src/app/modules/client/services/ride-request-state.service';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit, OnInit {

  setView(newLatLng: any, arg1: number) {
    throw new Error('Method not implemented.');
  }
  fitBounds(arg0: any) {
    throw new Error('Method not implemented.');
  }

  private map: L.Map;
  private customIcon : L.Icon;
  
  locations : Array<L.Marker> = new Array<L.Marker>();
  private featureGroup: L.FeatureGroup;

  routingControl: L.Routing.Control
  routingPlan: L.Routing.Plan

  private stompClient: any;
  driver: any = {};
  rides: any = {};

  @Input() containerId: string;

  private initMap(): void {

    this.customIcon = L.icon({
      iconUrl: 'https://www.freeiconspng.com/thumbs/pin-png/pin-png-28.png',
      iconSize: [30, 40],
      iconAnchor: [15, 20],
      popupAnchor: [-3, -76]
    })
    
    this.map = L.map(this.containerId, {
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

  constructor(private mapService: MapService, protected stateManagement: RideRequestStateService) {}

  public reset(){
    this.map.remove();
    this.initMap();
  }


  ngOnInit(): void {
    let ws = new SockJS(environment.apiURL + "/ws");
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
    this.getActiveRide();
  }

  getActiveRide():void{
    
  }

  openGlobalSocket() {
    this.stompClient.subscribe('/map-updates/update-ride-state', (message: { body: string }) => {
      let msg = JSON.parse(message.body);
      // console.log(msg);
      let mapRide: MapRide= JSON.parse(message.body);
      if(!this.driver || Object.keys(this.driver).length===0){
        let geoLayerRouteGroup: LayerGroup = new LayerGroup();
        let markerLayer = marker([mapRide.driver.latitude, mapRide.driver.longitude], {
          icon: icon({
            iconUrl: 'assets/car.png',
            iconSize: [35, 45],
            iconAnchor: [18, 45],
          }),
        });
        let coordinates:L.LatLng[] = mapRide.atomicPoints.map(point=>new L.LatLng(point.latitude, point.longitude));
        var polyline = L.polyline(coordinates, {color: 'red'}).addTo(this.map);
        markerLayer.addTo(geoLayerRouteGroup);
        this.driver = markerLayer;
        this.driver.addTo(this.map);
      }
      else{
        let existingVehicle = this.driver;
        existingVehicle.setLatLng([mapRide.driver.latitude, mapRide.driver.longitude]);
        existingVehicle.update();
      }
    });
  }

  ngAfterViewInit(): void {
    this.initMap();
    
    for(let i=0; i < 5; i++){
      this.locations.push(L.marker([0, 0], {icon: this.customIcon}));
    }        

    this.stateManagement.mapa = this;
  }
  
  pinNewResult(pin: MapSearchResult, i: number): void {  
    let newLatLng = new L.LatLng(parseFloat(pin.lat), parseFloat(pin.lon));

    this.locations[i].setLatLng(newLatLng);
    this.locations[i].addTo(this.map);

    this.map.setView(newLatLng, 12);    
    this.featureGroup = L.featureGroup(this.locations.filter((location) => { return location.getLatLng().lat !== 0 && location.getLatLng().lng !== 0; }));
    
    this.map.fitBounds(this.featureGroup.getBounds());

    this.mapService.automaticallyFindPath("Custom", this.locations);
  }

  getPins(): Array<L.Marker>{
    return this.locations;
  }

  deletePin(index: number): void{
    this.locations[index].removeFrom(this.map);
    this.locations.splice(index, 1);
    this.locations.push(L.marker([0, 0], {icon: this.customIcon}));
    // this.removePreviousRoute();
    this.mapService.automaticallyFindPath("Custom", this.locations);
  }

  removePreviousRoute(): void{
    this.map.removeControl(this.routingControl);
    this.map.eachLayer(layer => {
      if (layer instanceof L.Marker)
      { 
        this.map.removeLayer(layer)
      }
    })
    this.map.remove();
    this.initMap();
  }

  drawRoute(coords: any) {
    this.map.remove();
    this.initMap();
    var polyline = L.polyline(coords, {color: 'red'}).addTo(this.map);
   
    let locationCounter = this.placePinsForFoundPath();
    console.log(locationCounter);
    
    if(locationCounter > 1){
      this.map.fitBounds(polyline.getBounds());
    }
  }

  placePinsForFoundPath(): number{
    let locationCounter: number = 0;
    let locations = this.locations.filter((location) => {
      return location.getLatLng().lat !== 0 && location.getLatLng().lng !== 0;
    });

    for(let location of locations){
      locationCounter += 1;
      location.addTo(this.map);
    }
    return locationCounter;
  }
}
