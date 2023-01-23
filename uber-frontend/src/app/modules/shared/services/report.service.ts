import { DateRange } from './../../../model/DateRange';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/app/environments/environment';
import { ReportResponse } from 'src/app/model/ReportResponse';
import { TokenUtilsService } from './token-utils.service';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient, private tokenUtilsService: TokenUtilsService){
    
  }

  generateReportForDateRange(dateRange:DateRange, userEmail:string|null){
    let headers = new HttpHeaders();
    const loggedUser = this.tokenUtilsService.getUserFromToken();
    const startDate = new Date(dateRange.start?.getTime() as number + (12*60*60*1000))
    const endDate = new Date(dateRange.end?.getTime() as number + (12*60*60*1000))
    const data = {
      start:startDate,
      end:endDate,
      userEmail:userEmail?userEmail:loggedUser?.email,
      roleType:loggedUser?.role
    }

    return this.http.post<ReportResponse>(environment.apiURL + '/report/', data);   

  }
}
