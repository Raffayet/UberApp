import { Injectable } from '@angular/core';
import { environment } from './../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaypalService {

  constructor(private http: HttpClient) { }

  getAmountOfTokens(email: string){
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);

    return this.http.get<number>(environment.apiURL+"/client/get-tokens", {params:queryParams});
  }
}
