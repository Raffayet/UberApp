import { Component, OnInit } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { environment } from "../../../../environments/environment";
import { Observable } from "rxjs";
import { over, Client, Message as StompMessage} from 'stompjs';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';

@Component({
  selector: 'app-driver-notification',
  templateUrl: './driver-notification.component.html',
  styleUrls: ['./driver-notification.component.css']
})
export class DriverNotificationComponent implements OnInit{

  private stompClient : Client;
  loggedUser: User | null;

  constructor(private tokenUtilsService: TokenUtilsService){}

  ngOnInit() {
    this.loggedUser = this.tokenUtilsService.getUserFromToken();

    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);    
  }

  onConnected = () => {
    this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/driver-notification", this.onNotificationReceived);
  }

  onError = () => {
    console.log("Socket error.");    
  }

  onNotificationReceived = (payload: StompMessage) => {
    let payloadData = JSON.parse(payload.body);
    console.log("STIGAO REQUEST SA BEKA:");    
    console.log(payloadData);    
  }

}
