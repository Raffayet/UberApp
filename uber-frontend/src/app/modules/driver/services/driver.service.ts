import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/app/environments/environment';
import { TokenUtilsService } from '../../shared/services/token-utils.service';
import { RideToTake } from 'src/app/model/RideToTake';
import { DriveAssignature } from 'src/app/model/DriveAssignature';

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

  findAllRidesToTake(driverEmail: string) : Observable<RideToTake[]>{
   
    let queryParams = new HttpParams();
    queryParams = queryParams.append("format", "json");
    
    return this.http.get<RideToTake[]>(environment.apiURL + "/driver/get-rides-to-take", { params: queryParams});
  }

  resetAfterLogout(driverEmail: string){
    let headers = new HttpHeaders();
    return this.http.post<string>(environment.apiURL + "/driver/driver-logout", driverEmail, { headers, responseType: 'text' as 'json' });
  }

  assignDriveToDriver(driverEmail: string, id: number, initiatorEmail: string) {
    let driveAssignatureDto : DriveAssignature = {
      requestId: id,
      driverEmail: driverEmail,
      initiatorEmail: initiatorEmail
    }

    return this.http.post<string>(environment.apiURL + "/driver/assign-drive-to-driver", driveAssignatureDto);
  }
  
}
