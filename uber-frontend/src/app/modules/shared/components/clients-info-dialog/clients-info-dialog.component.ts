import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
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

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<ClientsInfoDialogComponent>, private mapService: MapService, private rideRequestStateService: RideRequestStateService)
    {
      if(data)
      {
        this.ride = data.ride;
      }
    }

    onCloseClick(): void {
      this.dialogRef.close(false);
    }
}
