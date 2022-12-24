import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { environment } from 'src/app/environments/environment';
import { TokenUtilsService } from './token-utils.service';
import { Request } from '../components/history/history.component';

@Injectable({
  providedIn: 'root'
})
export class RideService {

  constructor(private http: HttpClient, private tokenUtilsService: TokenUtilsService) { }

  getHistoryOfRides(request: Request): Observable<any>{
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");

    return this.http.get<any>(environment.apiURL + "/rides/get-all", { params: queryParams});
  }
}
