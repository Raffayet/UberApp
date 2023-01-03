import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-timer-dialog',
  templateUrl: './timer-dialog.component.html',
  styleUrls: ['./timer-dialog.component.css']
})
export class TimerDialogComponent {

  pickedTime: Date;

  minReserveTime: Date;

  maxReserveTime: Date;

  message: string = "Are you sure?"
  confirmButtonText = "Yes"
  cancelButtonText = "Cancel"
  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<TimerDialogComponent>) {
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
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  changeTime(): void{
    this.minReserveTime = new Date();
    this.maxReserveTime = new Date();
    this.maxReserveTime.setHours(this.maxReserveTime.getHours() + 5);
  }
}
