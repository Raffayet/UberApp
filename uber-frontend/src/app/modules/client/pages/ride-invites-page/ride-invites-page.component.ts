import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { environment } from 'src/app/environments/environment';
import { RideInvite } from 'src/app/model/RideInvite';
import { User } from 'src/app/model/User';
import { LivechatService } from 'src/app/modules/shared/services/livechat.service';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { ClientService } from '../../services/client.service';
import { over, Client, Message as StompMessage} from 'stompjs';
import { Ride } from 'src/app/model/Ride';
import { Output, EventEmitter } from '@angular/core';
import { ClientDashboardComponent } from '../client-dashboard/client-dashboard.component';

@Component({
  selector: 'app-requests-page',
  templateUrl: './ride-invites-page.component.html',
  styleUrls: ['./ride-invites-page.component.css']
})
export class RideInvitesPageComponent {
  stompClient: any;
  loggedUser: User | null;
  rideInvites: RideInvite[];
  clientDashboard: ClientDashboardComponent;

  @Output() rideInvitesEmitter = new EventEmitter<RideInvite[]>();

  constructor(private clientService: ClientService, private tokenUtilsService: TokenUtilsService) {}

  ngOnInit() { 
    
  }
  
}
