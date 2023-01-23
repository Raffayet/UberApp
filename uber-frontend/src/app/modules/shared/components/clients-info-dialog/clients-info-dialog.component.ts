import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MapSearchResult } from 'src/app/model/MapSearchResult';
import { Point } from 'src/app/model/Point';
import { Ride } from 'src/app/model/Ride';
import { MapService } from 'src/app/modules/client/services/map.service';
import { RideRequestStateService } from 'src/app/modules/client/services/ride-request-state.service';
import { RouteDetailDialogComponent } from '../route-detail-dialog/route-detail-dialog.component';

@Component({
  selector: 'app-clients-info-dialog',
  templateUrl: './clients-info-dialog.component.html',
  styleUrls: ['./clients-info-dialog.component.css']
})
export class ClientsInfoDialogComponent {
  ride: Ride;
  locations: MapSearchResult[];

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<ClientsInfoDialogComponent>, private mapService: MapService, private rideRequestStateService: RideRequestStateService)
    {
      if(data)
      {
        this.ride = data.ride;
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

    onCloseClick(): void {
      this.dialogRef.close(false);
    }
}
