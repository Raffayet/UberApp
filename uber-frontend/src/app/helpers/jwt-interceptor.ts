import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError, EMPTY } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from "../environments/environment";
import {Router} from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor(private router: Router, private toastr: ToastrService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let token = localStorage.getItem("user");
        const isApiUrl = request.url.startsWith(environment.apiURL);
        
        if(isApiUrl && token){
            request = request.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }       

        return next.handle(request).pipe(catchError(err => {
            if (err.status == 401) {
                this.router.navigateByUrl("/");
                this.toastr.warning("You were not authenticated, please log in!");
                return EMPTY;
            }
            const error = err.error?.message || err.statusText;
            return throwError(() => error);
        }));
    }
}