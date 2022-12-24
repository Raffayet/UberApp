import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { TokenUtilsService } from '../../shared/services/token-utils.service';

@Injectable({
  providedIn: 'root'
})
export class DriverService {

  constructor(private http: HttpClient, private tokenUtilsService: TokenUtilsService) { }

  updatePersonalInfo(infoForm: FormGroup) : Observable<string>{
    let headers = new HttpHeaders();
    let params = {...infoForm.getRawValue(), role: this.tokenUtilsService.getRoleFromToken()};

    return this.http.put<string>(environment.apiURL + '/driver/update-personal-info', params, { headers, responseType: 'text' as 'json' });      
  }

  
}
