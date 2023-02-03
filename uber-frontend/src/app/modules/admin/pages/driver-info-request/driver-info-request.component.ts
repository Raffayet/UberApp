import { ToastrService } from 'ngx-toastr';
import { DriverInfoDialogComponent } from './../../components/driver-info-dialog/driver-info-dialog.component';
import { DriverService } from './../../../driver/services/driver.service';
import { Component, OnInit } from '@angular/core';
import { DriverChangeRequest } from 'src/app/model/DriverInfoChangeRequest';
import { MatDialog } from '@angular/material/dialog';
import { environment } from 'src/app/environments/environment';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';

@Component({
  selector: 'app-driver-info-request',
  templateUrl: './driver-info-request.component.html',
  styleUrls: ['./driver-info-request.component.css']
})
export class DriverInfoRequestComponent implements OnInit{
  private stompClient: any;
  driverInfos:DriverChangeRequest[] = [];
  displayedColumns = ['id',  'name', 'surname', 'email', 'city', 'phone', 'accepted', 'show new data'];

  constructor(private driverService:DriverService, private dialog: MatDialog, private toastr:ToastrService){

  }


  ngOnInit(): void {
    this.loadDriverInfoRequest();

    let ws = new SockJS(environment.apiURL + "/ws");
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  loadDriverInfoRequest(){
    this.driverService.getDriverInfoRequests()
    .subscribe({
      next: (data) => {
        console.log(data);
        this.driverInfos = data;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe('/info-changed-request', (message: { body: string }) => {
      this.toastr.info("","New Driver Info Change Request");
      this.loadDriverInfoRequest();
    });

  }

  getNewInfo(elemet:DriverChangeRequest){
    this.openDialog(elemet);
  }

  openDialog(driverInfo: DriverChangeRequest)
  {
    const dialogRef = this.dialog.open(DriverInfoDialogComponent,{data:driverInfo });

    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if(confirmed === true){
        console.log("ACCEPTED");
        driverInfo.accepted = true;
        this.driverService.sendResponseToInfoChangeRequest(driverInfo).subscribe({
          next: (data) => {
            this.toastr.success("","Response saved");
            this.driverInfos = this.driverInfos.filter(di=>di.id !== data.id);
          },
          error: (err) => {
            this.toastr.error("","Something went wrong");
            console.log(err);
          },
        });;
      }
      else if(confirmed === false){
        console.log("REJECTED")
        driverInfo.accepted = false;
        this.driverService.sendResponseToInfoChangeRequest(driverInfo).subscribe({
          next: (data) => {
            this.toastr.success("","Response saved");
            console.log(this.driverInfos);
            console.log(data);
            this.driverInfos = this.driverInfos.filter(di=>di.id !== data.id);
          },
          error: (err) => {
            this.toastr.error("","Something went wrong");
            console.log(err);
          },
        });;
      }

    });
  }




}
