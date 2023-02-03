import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as L from 'leaflet';
import { environment } from 'src/app/environments/environment';
import { Point } from 'src/app/model/Point';

export type MapSearchResult = {
  displayName: string;
  lon: string;
  lat: string;
};

export type PathInfoDto = {
  distance: number,
  atomicPoints: Point[]
}

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

  calculatePrice(vehicleType: string, distance: number) {

    let queryParams = new HttpParams();
    queryParams = queryParams.append("totalDistance", distance);
    queryParams = queryParams.append("vehicleType", vehicleType);
    queryParams = queryParams.append("format", "json");

    return this.http.get<number>(environment.apiURL + "/rides/calculate-price", { params: queryParams});
  }

  automaticallyFindPath(routeType: string, locations: Array<L.Marker>): Observable<PathInfoDto>{
    locations = locations.filter(location => {
      return location.getLatLng().lat !== 0 && location.getLatLng().lng !== 0;
    });

    let points = locations.map(location => new Point(location.getLatLng().lat,
    location.getLatLng().lng));

    switch(routeType){
      case 'Custom':
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-custom-route", points);
      case 'Optimal':
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-optimal-route", points);
      case 'Alternative':
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-alternative-route", points);
      default:
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-custom-route", points);
    }
  }

  automaticallyFindPathForHistory(routeType: string, locations: MapSearchResult[]): Observable<PathInfoDto>{
    locations = locations.filter(location => {
      return parseFloat(location.lat) !== 0 && parseFloat(location.lon) !== 0;
    });

    let points = locations.map(location => new Point(parseFloat(location.lat),
    parseFloat(location.lon)));

    switch(routeType){
      case 'Custom':
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-custom-route", points);
      case 'Optimal':
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-optimal-route", points);
      case 'Alternative':
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-alternative-route", points);
      default:
        return this.http.post<PathInfoDto>(environment.apiURL + "/map/determine-custom-route", points);
    }
  }
  
  getActiveDriver():Observable<any>{
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");

    return this.http.get<any>(environment.apiURL + "/rides/active-driver", { params: queryParams});

  }
  
}
