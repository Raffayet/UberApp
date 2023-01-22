import { TokenUtilsService } from './../../services/token-utils.service';
import {OnInit, Component, Input, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { RideService } from '../../services/ride.service';
import { Ride } from 'src/app/model/Ride';
import { MatSort, SortDirection } from '@angular/material/sort';
import { catchError, map, merge, Observable, startWith, switchMap } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { RideInviteDialogComponent } from 'src/app/modules/client/components/ride-invite-dialog/ride-invite-dialog.component';
import { RouteDetailDialogComponent } from '../route-detail-dialog/route-detail-dialog.component';
import { MapSearchResult } from 'src/app/model/MapSearchResult';
import { DriverService } from 'src/app/modules/driver/services/driver.service';
import { DriverInfo } from 'src/app/model/DriverInfo';
import { MapComponent } from '../map/map.component';


export interface Request {
  page: number;
  size: number;
}

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})

export class HistoryComponent implements OnInit{

  @Input() email: string;
  rides: Ride[] = [];
  totalElements: number = 0;

  displayedColumns: string[] = ['id', 'price', 'firstLocation', 'destination', 'startTime', 'endTime', 'buttonsColumn'];
  isLoadingResults = true;
  isRateLimitReached = false;
  resultsLength = 0;
  currentDriverInfo: DriverInfo;

  data: GithubIssue[] = [];

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private rideService: RideService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private driverService: DriverService){}

  ngOnInit() {
    if(this.tokenUtilsService.getRoleFromToken() != "ADMIN"){      
      this.email = this.tokenUtilsService.getUsernameFromToken() as string;
    }
    this.getHistoryOfRides({ page: 0, size: 5 });
  } 

  ngAfterViewInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
  }

  convertStartDateFormat(){
    for(let i = 0; i < this.rides.length; i++)
    {
      let year = this.rides[i].startTime[0];
      let month = this.rides[i].startTime[1];
      let day = this.rides[i].startTime[2];
      let hours = this.rides[i].startTime[3];
      let minutes = this.rides[i].startTime[4];
      let seconds = this.rides[i].startTime[5];
      let formattedStartTime = day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
      this.rides[i].formattedStartTime = formattedStartTime;
    }
  }

  convertEndDateFormat(){
    for(let i = 0; i < this.rides.length; i++)
    {
      let year = this.rides[i].startTime[0];
      let month = this.rides[i].startTime[1];
      let day = this.rides[i].startTime[2];
      let hours = this.rides[i].startTime[3];
      let minutes = this.rides[i].startTime[4];
      let seconds = this.rides[i].startTime[5];
      let formattedEndTime = day + "/" + month + "/" + year + " " + hours + ":" + minutes + ":" + seconds;
      this.rides[i].formattedEndTime = formattedEndTime;
    }
  }

  private getHistoryOfRides(request: Request){
      this.rideService.getHistoryOfRides(request, this.email)
      .subscribe({
          next: (data) => {
            this.rides = data.content;
            this.totalElements = data.size;
            this.convertStartDateFormat();
            console.log(this.rides);
            this.convertEndDateFormat();
          },
          error: (err) => {
            console.log(err.error.message);
          },
        });
  }

  nextPage(event: PageEvent) {
    let request: Request = {'page': event.pageIndex, 'size': event.pageSize};
  
    this.getHistoryOfRides(request);
  }

  getDriverInfo(locations: MapSearchResult[], rideId: number) {
    this.driverService.getDriverInfoByRideId(rideId)
      .subscribe({
        next: (data) => {
          this.currentDriverInfo = data;
          this.openDialog(data, locations);
        },
        error: (error) => {
          console.error(error);
        }
      });
  }

  openDialog(driverInfo: DriverInfo, locations: MapSearchResult[])
  {
    const dialogRef = this.dialog.open(RouteDetailDialogComponent,{
      
      data:{
        email: driverInfo.email,
        name: driverInfo.name,
        lastName: driverInfo.lastName,
        averageRating: driverInfo.averageRating,
        locations: locations
      }
    });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      
    });
  }

}

export interface GithubApi {
  items: GithubIssue[];
  total_count: number;
}

export interface GithubIssue {
  id: number;
  price: number;
}
