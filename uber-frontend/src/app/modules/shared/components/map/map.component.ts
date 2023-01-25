import { RideReviewComponent } from './../../../client/components/ride-review/ride-review.component';
import { ToastrService } from 'ngx-toastr';
import { MapRide } from './../../../../model/MapRide';
import * as L from 'leaflet';
import { AfterViewInit, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MapSearchResult } from 'src/app/modules/client/services/map.service';
import { MapService } from 'src/app/modules/client/services/map.service';
import 'leaflet-routing-machine';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { environment } from 'src/app/environments/environment';
import {  icon, LayerGroup, marker } from 'leaflet';
import { RideRequestStateService } from 'src/app/modules/client/services/ride-request-state.service';
import { TokenUtilsService } from '../../services/token-utils.service';
import { MatDialog } from '@angular/material/dialog';
import { DriverService } from 'src/app/modules/driver/services/driver.service';
import { User } from 'src/app/model/User';

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
  polylinesBeforeRide: L.Polyline<any>[] = [];
  polylinesRide: L.Polyline<any>[] = [];
  private toastNotification: any;

  loggedUser: User | null;
  driverEmail: string;

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

  constructor(private mapService: MapService, protected stateManagement: RideRequestStateService, private toaster:ToastrService, private tokenUtilsService: TokenUtilsService, public dialog: MatDialog, private driverService: DriverService) {}

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
    this.loggedUser = this.tokenUtilsService.getUserFromToken(); 
  }

  openDialog(driverId: number):void{
    
    const dialogRef = this.dialog.open(RideReviewComponent);

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.getDriverEmailById(driverId, result.rating, result.comment);
      }
    });
  }

  setTimerForRatingExpiration(driverId: number, rideId: number)
  {
    this.driverService.setTimerForRatingExpiration(rideId)
    .subscribe({
      next: data => {
        console.log(data);
        this.openDialog(driverId);
      },
      error: error => {
        console.error(error);
      }
    });
  }

  getDriverEmailById(driverId: number, numberOfStars: number, comment: string)
  {
    this.driverService.getDriverEmailById(driverId).subscribe({
      next: data => {
        console.log(data);
        this.driverEmail = data;
        this.rateDriver(numberOfStars, comment);
      },
      error: error => {
        console.error(error);
      }
    });
  }

  rateDriver(numberOfStars: number, comment: string) {
    this.driverService.rateDriver(numberOfStars, comment, this.loggedUser?.email as string, this.driverEmail)
      .subscribe({
        next: (data) => {
          console.log(data);
        },
        error: (error) => {
          console.error(error);
        }
      });
  }
  
  openGlobalSocket() {
    let loggedUser = this.tokenUtilsService.getUserFromToken();

    this.stompClient.subscribe("/user/" + loggedUser?.email  + '/map-updates/update-ride-state', (message: { body: string }) => {
      let msg = JSON.parse(message.body);
      let geoLayerRouteGroup: LayerGroup = new LayerGroup();
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
        let coordinatesBeforeRide:L.LatLng[] = mapRide.atomicPointsBeforeRide.map(point=>new L.LatLng(point.latitude, point.longitude));
        let p = L.polyline(coordinatesBeforeRide, {color: 'green'}).addTo(this.map);
        let p1 = L.polyline(coordinates, {color: 'red'}).addTo(this.map);
        this.toastNotification = this.toaster.info(`Remaining time: ${mapRide.duration}s`,"Waiting for driver", {timeOut: 0});
        this.polylinesBeforeRide.push(p);
        this.polylinesRide.push(p1);
        markerLayer.addTo(geoLayerRouteGroup);
        this.driver = markerLayer;
        this.driver.addTo(this.map);
      }
      else{
        let existingVehicle = this.driver;
        existingVehicle.setLatLng([mapRide.driver.latitude, mapRide.driver.longitude]);
        let coordinates:L.LatLng[] = mapRide.atomicPoints.map(point=>new L.LatLng(point.latitude, point.longitude));
        let coordinatesBeforeRide:L.LatLng[] = mapRide.atomicPointsBeforeRide.map(point=>new L.LatLng(point.latitude, point.longitude));
        let that = this;
        this.polylinesBeforeRide.forEach(function (item) {
          that.map.removeLayer(item);
        });

        if (this.toaster.currentlyActive > 0) {
          let componentInstance = this.toastNotification.toastRef.componentInstance;
          componentInstance.message = `Remaining time: ${mapRide.duration}s`;
          componentInstance.title = mapRide.status === "WAITING"?"Waiting for driver":"Ride ends";
        }else{
          this.toastNotification = this.toaster.info(`Remaining time: ${mapRide.duration}s`,mapRide.status === "WAITING"?"Waiting for driver":"Ride ends",{timeOut: 0,});
        }

        if(mapRide.status === "WAITING"){
          let index = coordinatesBeforeRide.findIndex(cord=>cord.lat===mapRide.driver.latitude && cord.lng ===mapRide.driver.longitude);
          coordinatesBeforeRide = coordinatesBeforeRide.slice(index);
          let p = L.polyline(coordinatesBeforeRide, {color: 'green'}).addTo(this.map);
          this.polylinesBeforeRide.push(p);
        }
        else{
          this.polylinesRide.forEach(function (item) {
            that.map.removeLayer(item);
          });
          coordinates = coordinates.slice(coordinates.findIndex(cord=>cord.lat===mapRide.driver.latitude && cord.lng ===mapRide.driver.longitude))
          let p = L.polyline(coordinates, {color: 'red'}).addTo(this.map);
          this.polylinesRide.push(p);
        }
        existingVehicle.update();
      }
    });

    this.stompClient.subscribe("/user/" + loggedUser?.email  + '/map-updates/ended-ride', (message: { body: string }) => {
      const mapRide: MapRide = JSON.parse(message.body);
      let that = this;
      this.polylinesBeforeRide.forEach(function (item) {
        that.map.removeLayer(item);
      });
      this.map.removeLayer(this.driver);
      this.driver = {}
      this.toaster.clear();
      this.toaster.info(`Ride successfully ended`,"Ride ended", {timeOut: 5000});

      this.setTimerForRatingExpiration(mapRide.driver.id, mapRide.id);
      
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
