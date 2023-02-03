import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { environment } from 'src/app/environments/environment';

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

  addAmountOfTokens(email: string, amount: number): Observable<number | string>{
    let queryParams = new HttpParams();
    queryParams = queryParams.append("email", email);
    queryParams = queryParams.append("amount", amount);

    return this.http.get<number | string>(environment.apiURL+"/client/add-tokens", {params:queryParams});
  }

}
