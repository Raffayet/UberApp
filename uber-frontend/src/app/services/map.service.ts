import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

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

}
