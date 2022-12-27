import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as L from 'leaflet';
import { MapComponent } from 'src/app/modules/shared/components/map/map.component';
import { environment } from 'src/app/environments/environment';
import { Point } from 'src/app/model/Point';

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

  calculateTotalDistance(locations: Array<L.Marker>): number{
    let totalDistance = 0
    for (let i = 0; i < locations.length - 1; i++)
    {
      if (locations[i].getLatLng().lat !== 0 && locations[i].getLatLng().lng !== 0 && locations[i + 1].getLatLng().lat !== 0 && locations[i + 1].getLatLng().lng !== 0)
        totalDistance += locations[i].getLatLng().distanceTo(locations[i + 1].getLatLng());
    }
    return totalDistance / 1000;
  }

  calculatePrice(vehicleType: string, locations: Array<L.Marker>) {
    let totalDistance = this.calculateTotalDistance(locations)

    let queryParams = new HttpParams();
    queryParams = queryParams.append("totalDistance", totalDistance);
    queryParams = queryParams.append("vehicleType", vehicleType);
    queryParams = queryParams.append("format", "json");

    return this.http.get<number>(environment.apiURL + "/rides/calculate-price", { params: queryParams});
  }

  getTotalDistance(locations: Array<L.Marker>){
    let totalDistance = this.calculateTotalDistance(locations)
    return totalDistance
  }

  automaticallyFindPath(routeType: string, locations: Array<L.Marker>): Observable<Array<Point>>{
    locations = locations.filter(location => {
      return location.getLatLng().lat !== 0 && location.getLatLng().lng !== 0;
    });

    let points = locations.map(location => new Point(location.getLatLng().lat,
    location.getLatLng().lng));

    switch(routeType){
      case 'Custom':
        return this.http.post<Array<Point>>(environment.apiURL + "/map/determine-custom-route", points);
      case 'Optimal':
        return this.http.post<Array<Point>>(environment.apiURL + "/map/determine-optimal-route", points);
      case 'Alternative':
        return this.http.post<Array<Point>>(environment.apiURL + "/map/determine-alternative-route", points);
      default:
        return this.http.post<Array<Point>>(environment.apiURL + "/map/determine-custom-route", points);
    }
  }
}
