import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from "../environments/environment";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    constructor() { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let token = localStorage.getItem("user");
        const isApiUrl = request.url.startsWith(environment.apiURL);
        
        if(isApiUrl && token){
            request = request.clone({
                setHeaders: { Authorization: `Bearer ${token}` }
            });
        }       

        return next.handle(request);
    }
}