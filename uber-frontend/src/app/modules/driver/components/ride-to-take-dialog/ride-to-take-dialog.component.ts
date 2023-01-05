import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-ride-to-take-dialog',
  templateUrl: './ride-to-take-dialog.component.html',
  styleUrls: ['./ride-to-take-dialog.component.css']
})
export class RideToTakeDialogComponent {
  from: string = "";
  firstLocation: string = "";
  destination: string = "";
  drivingTime: Date;
  confirmButtonText = "Yes"
  cancelButtonText = "Cancel"
  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<RideToTakeDialogComponent>) {
      if(data){
        this.from = data.from;
        this.firstLocation = data.firstLocation;
        this.destination = data.destination;
        this.drivingTime = data.drivingTime;
        if (data.buttonText) {
          this.confirmButtonText = data.buttonText.ok || this.confirmButtonText;
          this.cancelButtonText = data.buttonText.cancel || this.cancelButtonText;
        }
      }
  }

  onConfirmClick(): void {
    this.dialogRef.close(true);
  }

  onCancelClick(): void {
    this.dialogRef.close(false);
  }
}
