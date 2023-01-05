import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { RideRequestStateService } from '../../services/ride-request-state.service';
import * as moment from 'moment';

@Component({
  selector: 'app-timer-dialog',
  templateUrl: './timer-dialog.component.html',
  styleUrls: ['./timer-dialog.component.css']
})
export class TimerDialogComponent {

  maxReserveTime: Date;

  minReserveTime = moment().toDate();

  selectedTime: Date;

  message: string = "Are you sure?"
  confirmButtonText = "Yes"
  cancelButtonText = "Cancel"
  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<TimerDialogComponent>,
    protected stateManagement: RideRequestStateService) {

      if(data){
        this.message = data.message || this.message;
        if (data.buttonText) {
          this.confirmButtonText = data.buttonText.ok || this.confirmButtonText;
          this.cancelButtonText = data.buttonText.cancel || this.cancelButtonText;
        }
      }
  }
  onConfirmClick(): void {
    this.dialogRef.close(true);
    this.stateManagement.rideRequest.isReserved = true;
  }

  onCancelClick(): void {
    this.dialogRef.close(false);
    this.stateManagement.rideRequest.isReserved = false;
  }

  changeTime(): void{
    this.minReserveTime = new Date();
    this.maxReserveTime = new Date();
    this.maxReserveTime.setHours(this.maxReserveTime.getHours() + 5);
  }
}
