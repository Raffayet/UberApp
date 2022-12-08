import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as L from 'leaflet';
import { MapComponent } from '../components/map/map.component';
import { environment } from '../environments/environment';

export type MapSearchResult = {
  displayName: string;
  lon: string;
  lat: string;
};

@Injectable({
  providedIn: 'root'
})
export class MapService {

  constructor(private http: HttpClient) { }

  convertSearchResultsToList(results: any[]): MapSearchResult[]{
      let retList = [];

      for(let result of results){
        let element = <MapSearchResult>({
          displayName: result.display_name,
          lon: result.lon,
          lat: result.lat
       });
          retList.push(element);
      }

      return retList;
  }

  search(query: string) : Observable<object[]>{
   
    let queryParams = new HttpParams();
    queryParams = queryParams.append("q", query);
    queryParams = queryParams.append("format", "json");

    return this.http.get<object[]>("https://nominatim.openstreetmap.org/search", { params: queryParams});
  }

  calculateTotalDistance(locations: Array<L.Marker>): Number{
    let totalDistance = 0
    for (let i = 0; i < locations.length; i++)
    {
      if (locations[i].getLatLng().lat !== 0 && locations[i].getLatLng().lng !== 0 && locations[i + 1].getLatLng().lat !== 0 && locations[i + 1].getLatLng().lng !== 0)
        totalDistance += locations[i].getLatLng().distanceTo(locations[i + 1].getLatLng())
    }
    return totalDistance / 1000
  }

  calculatePrice(vehicleType: string, locations: Array<L.Marker>) {
    let vehicleTypeMap = new Map();
    vehicleTypeMap.set("Regular", 200);   //pocetna cena u zavisnosti od tipa vozila
    vehicleTypeMap.set("Baby Seats", 300);
    vehicleTypeMap.set("Pet Seats", 250);

    let totalDistance = this.calculateTotalDistance(locations)
    let price = (vehicleTypeMap.get(vehicleType) + Number(totalDistance) * 120) / environment.usdRsdRatio
    let roundedPrice = Math.round(price * 100) / 100
    return roundedPrice;
  }
}
