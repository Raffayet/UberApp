import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RideToTake } from 'src/app/model/RideToTake';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { over, Client, Message as StompMessage} from 'stompjs';
import { DriverService } from '../../services/driver.service';

@Component({
  selector: 'app-driver-dashboard',
  templateUrl: './driver-dashboard.component.html',
  styleUrls: ['./driver-dashboard.component.css']
})
export class DriverDashboardComponent {

  chatHidden:boolean = false;
  navbarLabels: string[] = ["Profile", "Rides To Do", "Report"];
  navbarPaths: string[] = ["profile-page", "rides-to-do", 'report'];
  option: string = this.navbarPaths[0];

  loggedDriver: User | null;
  stompClient: Client;

  changeOption(eventData: string): void{      
    this.router.navigate(['/driver', {outlets: {'DriverRouter': [eventData]}}]);
  }

  constructor(private router: Router, private userService: UserService, private tokenUtilsService: TokenUtilsService, private dialog: MatDialog, private driverService: DriverService){}

  ngOnInit() {
    this.loggedDriver = this.tokenUtilsService.getUserFromToken(); 
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
