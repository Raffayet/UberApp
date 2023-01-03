import { TokenUtilsService } from './../../services/token-utils.service';
import {OnInit, Component, Input} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatTableDataSource} from '@angular/material/table';
import { PageEvent } from '@angular/material/paginator';
import { RideService } from '../../services/ride.service';
import { Ride } from 'src/app/model/Ride';
export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

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

  displayedColumns: string[] = ['position'];

  constructor(private rideService: RideService, private tokenUtilsService: TokenUtilsService){}

  ngOnInit() {
    if(this.tokenUtilsService.getRoleFromToken() != "ADMIN"){      
      this.email = this.tokenUtilsService.getUsernameFromToken() as string;
    }
    this.getHistoryOfRides({ page: 0, size: 5 });
  }

  private getHistoryOfRides(request: Request){
      this.rideService.getHistoryOfRides(request, this.email)
      .subscribe({
          next: (data) => {
            this.rides = data.content;
            this.totalElements = data.size;
            console.log(data);
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

}
