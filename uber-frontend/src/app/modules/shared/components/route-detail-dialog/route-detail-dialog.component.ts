import { Component, Inject, OnInit, ViewChild, AfterViewInit  } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MapSearchResult } from 'src/app/model/MapSearchResult';
import { MapComponent } from '../map/map.component';
import { RideRequestStateService } from 'src/app/modules/client/services/ride-request-state.service';
import { HttpClient } from '@angular/common/http';
import { MapService } from 'src/app/modules/client/services/map.service';
import { Point } from 'src/app/model/Point';

@Component({
  selector: 'app-route-detail-dialog',
  templateUrl: './route-detail-dialog.component.html',
  styleUrls: ['./route-detail-dialog.component.css'],
})
export class RouteDetailDialogComponent {
  email: string;
  name: string;
  lastName: string;
  averageRating: number;
  locations: MapSearchResult[];
  httpClient: HttpClient;

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<RouteDetailDialogComponent>, private mapService: MapService, private rideRequestStateService: RideRequestStateService) {
      if(data){
        this.email = data.email;
        this.name = data.name;
        this.lastName = data.lastName;
        this.averageRating = data.averageRating;
        this.locations = data.locations;
        this.rideRequestStateService.mapa.pinNewResult(this.locations[0], 0);
        this.rideRequestStateService.mapa.pinNewResult(this.locations[1], 1);
        
        if(this.locations.length === 3)
        {
          this.rideRequestStateService.mapa.pinNewResult(this.locations[2], 2);
        }

        else if(this.locations.length === 4)
        {
          this.rideRequestStateService.mapa.pinNewResult(this.locations[3], 3);
        }

        else if(this.locations.length === 5)
        {
          this.rideRequestStateService.mapa.pinNewResult(this.locations[4], 4);
        }

        this.automaticallyFindPath('Custom');
      }
  }

  onCloseClick(): void {
    this.rideRequestStateService.reset();
    this.dialogRef.close(false);
  }

  automaticallyFindPath(routeType: string): void{
    this.mapService.automaticallyFindPathForHistory(routeType, this.locations).subscribe({
      next: data => {
          console.log(data);
          let coords: Array<Point> = data.atomicPoints;
          coords = coords.map(coord => new Point(coord.lat, coord.lng));
          this.rideRequestStateService.mapa.drawRoute(coords);
      },
      error: error => {
          console.error('There was an error!', error);
      }
    });
  }
}

