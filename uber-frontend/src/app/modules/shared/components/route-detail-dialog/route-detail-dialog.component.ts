import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-route-detail-dialog',
  templateUrl: './route-detail-dialog.component.html',
  styleUrls: ['./route-detail-dialog.component.css']
})
export class RouteDetailDialogComponent {
  email: string;
  name: string;
  lastName: string;
  averageRating: number;
  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<RouteDetailDialogComponent>) {
      if(data){
        this.email = data.email;
        this.name = data.name;
        this.lastName = data.lastName;
        this.averageRating = data.averageRating;
      }
  }

  onCloseClick(): void {
    this.dialogRef.close(false);
  }
}
