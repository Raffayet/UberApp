import { BlockUserRequest } from './../../../../model/BlockUserRequest';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { environment } from 'src/app/environments/environment';
import { RideToTake } from 'src/app/model/RideToTake';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { over, Client, Message as StompMessage} from 'stompjs';
import { DriverService } from '../../services/driver.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-driver-dashboard',
  templateUrl: './driver-dashboard.component.html',
  styleUrls: ['./driver-dashboard.component.css']
})
export class DriverDashboardComponent {

  chatHidden:boolean = false;
  navbarLabels: string[] = ["Profile", "Rides To Do", "History", "Report"];
  navbarPaths: string[] = ["profile-page", "rides-to-do", "history", 'report'];
  option: string = this.navbarPaths[0];

  loggedDriver: User | null;
  stompClient: any;

  changeOption(eventData: string): void{      
    this.router.navigate(['/driver', {outlets: {'DriverRouter': [eventData]}}]);
  }

  constructor(private router: Router, private userService: UserService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private driverService: DriverService, private toaster:ToastrService){}

  ngOnInit() {
    this.loggedDriver = this.tokenUtilsService.getUserFromToken(); 
    this.blockedUserEvent();
  }

  blockedUserEvent(){
    let ws = new SockJS(environment.apiURL + "/ws");
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe("/user/" + this.loggedDriver?.email  + '/blocked-user', (message: { body: string }) => {
      let blockUserRequest: BlockUserRequest= JSON.parse(message.body);
      this.toaster.warning(`You have been blocked because: ${blockUserRequest.description}\nYou won't be selected in new rides.`, "Blocked", {timeOut:20000});
    });

  }


  toggleChat = () => {
    this.chatHidden = !this.chatHidden;
  }

  logout = () => {     
    this.driverService.resetAfterLogout(this.loggedDriver?.email as string).subscribe();

    this.userService.changeUserDrivingStatus(this.tokenUtilsService.getUsernameFromToken() as string, 1).subscribe();
    localStorage.removeItem("user");
    this.router.navigateByUrl('/');
  }
}
