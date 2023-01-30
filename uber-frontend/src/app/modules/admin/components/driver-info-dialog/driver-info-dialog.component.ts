import { Component, Inject, Input, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DriverChangeRequest } from 'src/app/model/DriverInfoChangeRequest';

@Component({
  selector: 'app-driver-info-dialog',
  templateUrl: './driver-info-dialog.component.html',
  styleUrls: ['./driver-info-dialog.component.css']
})
export class DriverInfoDialogComponent implements OnInit{
  driverInfo:DriverChangeRequest;

  constructor(@Inject(MAT_DIALOG_DATA) private data: any){
    this.driverInfo = data;
  }

  ngOnInit(): void {
  }

}
