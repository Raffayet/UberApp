import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-ride-invite-dialog',
  templateUrl: './ride-invite-dialog.component.html',
  styleUrls: ['./ride-invite-dialog.component.css']
})
export class RideInviteDialogComponent {
  from: string = "";
  firstLocation: string = "";
  destination: string = "";
  priceToPay: string = "";
  confirmButtonText = "Yes";
  cancelButtonText = "Cancel";
  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<RideInviteDialogComponent>) {
      if(data){
        this.from = data.from;
        this.firstLocation = data.firstLocation;
        this.destination = data.destination;
        this.priceToPay = data.priceToPay;
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
    this.dialogRef.close(true);
  }
}
