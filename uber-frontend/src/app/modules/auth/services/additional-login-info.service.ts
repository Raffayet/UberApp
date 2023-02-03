import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { SocialLoginData } from 'src/app/model/SocialLoginData';

@Injectable({
  providedIn: 'root'
})
export class AdditionalLoginInfoService {

  constructor(private http: HttpClient) { }

  loginSocialUser(socialLoginData: SocialLoginData):Observable<String>{
    console.log(socialLoginData);

    return this.http.post<String>(environment.apiURL + "/user/social-login", socialLoginData)
            .pipe(
              catchError(this.handleError<String>('getHeroes', ""))
            );;
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
