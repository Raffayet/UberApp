import { Component, Input, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/User';
import { UserProfilePageComponent } from '../user-profile-page/user-profile-page.component';
import { UserService } from 'src/app/services/user.service';
import { TokenUtilsService } from 'src/app/services/token-utils.service';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;
    navbarLabels: string[] = ["Home", "Profile"];
    navbarPaths: string[] = ["/client-dashboard", "/user-profile"];
    option: string = this.navbarPaths[0];

    changeOption(eventData: string): void{      
      this.option = eventData;
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
