import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, of } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { DriverRegister } from 'src/app/model/DriverRegister';
import { Register } from 'src/app/model/Register';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
 

  constructor(private http: HttpClient) { }

  register(registrationForm: Register):Observable<String>{
    console.log(registrationForm);

    return this.http.post<String>(environment.apiURL+"/user/",registrationForm)
            .pipe(
              catchError(this.handleError<String>('register', ""))
            );
  }

  registerDriver(registerDto: DriverRegister):Observable<String>{
    console.log(registerDto);

    return this.http.post<String>(environment.apiURL+"/driver/",registerDto)
            .pipe(
              catchError(this.handleError<String>('registerDriver', ""))
            );
  }

  /**
 * Handle Http operation that failed.
 * Let the app continue.
 *
 * @param operation - name of the operation that failed
 * @param result - optional value to return as the observable result
 */
private handleError<T>(operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {

    // TODO: send the error to remote logging infrastructure
    console.error(error); // log to console instead

    // TODO: better job of transforming error for user consumption
    // this.log(`${operation} failed: ${error.message}`);

    // Let the app keep running by returning an empty result.
    return of(result as T);
  };
}
}
