import { Component, Input, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { UserProfilePageComponent } from '../../../shared/pages/user-profile-page/user-profile-page.component';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;
    navbarLabels: string[] = ["Home", "Profile", "Ride Invites"];
    navbarPaths: string[] = ["request-ride-page", "profile-page", "ride-invites"];
    option: string = this.navbarPaths[0];

    changeOption(eventData: string): void{      
      this.router.navigate(['/client', {outlets: {'ClientRouter': [eventData]}}]);
    }

    constructor(private router: Router, private userService: UserService, private tokenUtilsService: TokenUtilsService){}

    ngOnInit() {}

    toggleChat = () => {
      this.chatHidden = !this.chatHidden;
    }

    logout = () => {      
      this.userService.changeUserDrivingStatus(this.tokenUtilsService.getUsernameFromToken() as string, 1).subscribe();
      localStorage.removeItem("user");
      this.router.navigateByUrl('/');
    }
}
