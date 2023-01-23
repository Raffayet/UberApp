import { TokenUtilsService } from './../../services/token-utils.service';
import {OnInit, Component, Input, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { RideService } from '../../services/ride.service';
import { Ride } from 'src/app/model/Ride';
import { MatSort, SortDirection } from '@angular/material/sort';
import { catchError, map, merge, Observable, startWith, switchMap } from 'rxjs';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { RideInviteDialogComponent } from 'src/app/modules/client/components/ride-invite-dialog/ride-invite-dialog.component';
import { RouteDetailDialogComponent } from '../route-detail-dialog/route-detail-dialog.component';
import { MapSearchResult } from 'src/app/model/MapSearchResult';
import { DriverService } from 'src/app/modules/driver/services/driver.service';
import { DriverInfo } from 'src/app/model/DriverInfo';
import { MapComponent } from '../map/map.component';
import { ClientService } from 'src/app/modules/client/services/client.service';
import { OrderExistingRideDialogComponent } from '../order-existing-ride-dialog/order-existing-ride-dialog.component';
import { User } from 'src/app/model/User';
import { PaypalService } from '../../services/paypal.service';
import { ClientsInfoDialogComponent } from '../clients-info-dialog/clients-info-dialog.component';
import { FormControl } from '@angular/forms';
import { UserService } from '../../services/user.service';


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

  displayedColumns: string[];
  displayedColumnsClients = ['id', 'price', 'firstLocation', 'destination', 'startTime', 'endTime', 'buttonsColumn', 'buttonsColumn2'];
  displayedColumnsDrivers = ['id', 'price', 'firstLocation', 'destination', 'startTime', 'endTime', 'clientsInfo'];
  isLoadingResults = true;
  isRateLimitReached = false;
  resultsLength = 0;
  currentDriverInfo: DriverInfo;

  data: GithubIssue[] = [];
  loggedUser: User | null;

  currentAmount: number;
  protected userType: string;
  protected watchedUserType: string;
  emailControl = new FormControl('');
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  constructor(private rideService: RideService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private driverService: DriverService, private orderDialog: MatDialog, private paypalService: PaypalService, private clientsDialog: MatDialog, private userService: UserService){}

  ngOnInit() {
    if(this.tokenUtilsService.getRoleFromToken() == "CLIENT"){   
      this.userType = "CLIENT";
      this.email = this.tokenUtilsService.getUsernameFromToken() as string;
      this.displayedColumns = this.displayedColumnsClients;
      this.getHistoryOfRides({ page: 0, size: 5 });
      this.loggedUser = this.tokenUtilsService.getUserFromToken(); 
      this.getAmountOfTokens();
    }

    else if(this.tokenUtilsService.getRoleFromToken() == "DRIVER")
    {
      this.userType = "DRIVER";
      this.email = this.tokenUtilsService.getUsernameFromToken() as string;
      this.displayedColumns = this.displayedColumnsDrivers;
      this.getHistoryOfDriversRides({ page: 0, size: 5 });
    }

    else if (this.tokenUtilsService.getRoleFromToken() == "ADMIN")
    {
      this.userType = "ADMIN";
      this.getUsers();
    }
  } 

  ngAfterViewInit() {
    // If the user changes the sort order, reset back to the first page.
    this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
  }

  generateTableForUser()
  {
    this.getUserTypeByEmail();
  }

  generateDataByUser()
  {
    if(this.watchedUserType === "CLIENT")
    {
      this.displayedColumns = this.displayedColumnsClients;
      this.getHistoryOfRides({ page: 0, size: 5 });
    }

    else if(this.watchedUserType === "DRIVER")
    {
      this.displayedColumns = this.displayedColumnsDrivers;
      this.getHistoryOfDriversRides({ page: 0, size: 5 });
    }
  }

  getUserTypeByEmail()
  {
    this.userService.getUserTypeByEmail(this.emailControl.value as string).subscribe({
      next: (data: string) => {
        this.watchedUserType = data;
        this.generateDataByUser();
      },
      error: (err: HttpErrorResponse) => {
        console.log(err.error.message);
      },
    });
  }

  getUsers() {
    this.userService.getUsers().subscribe({
      next: (data:string[]) => {
        console.log(data);
        this.options = ['All Users'];
        this.options.push(...data);
        this.filteredOptions = this.emailControl.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '')),
        );
      },
      error: (err: HttpErrorResponse) => {
        console.log(err.error.message);
      },
    });
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  getAmountOfTokens(){
    this.paypalService.getAmountOfTokens(this.loggedUser?.email as string).subscribe(      
      (data: number) => this.currentAmount = data
    );
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
      this.rideService.getHistoryOfRides(request, this.emailControl.value ? this.emailControl.value : this.email)
      .subscribe({
          next: (data) => {
            console.log(data);
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

  private getHistoryOfDriversRides(request: Request)
  {
    this.rideService.getHistoryOfDriversRides(request, this.emailControl.value ? this.emailControl.value : this.email)
      .subscribe({
          next: (data) => {
            this.rides = data.content;
            this.totalElements = data.size;
            this.convertStartDateFormat();
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

  openOrderDialog(locations: MapSearchResult[], ride: Ride)
  {
    const orderDialogRef = this.orderDialog.open(OrderExistingRideDialogComponent,{
      
      data:{
        locations: locations,
        ride: ride
      }
    });

    orderDialogRef.afterClosed().subscribe((confirmed: boolean) => {
      
    });
  }

  openClientsDialog(locations: MapSearchResult[], ride: Ride)
  {
    console.log(ride);
    const clientsDialogRef = this.clientsDialog.open(ClientsInfoDialogComponent,{
      
      data:{
        ride: ride,
        locations: locations
      }
    });

    clientsDialogRef.afterClosed().subscribe((confirmed: boolean) => {
      
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
