import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { TokenUtilsService } from 'src/app/services/token-utils.service';

@Component({
  selector: 'app-driver-dashboard',
  templateUrl: './driver-dashboard.component.html',
  styleUrls: ['./driver-dashboard.component.css']
})
export class DriverDashboardComponent {

  chatHidden:boolean = false;
  navbarLabels: string[] = ["Home", "Profile"];
  navbarPaths: string[] = ["/driver-dashboard", "/user-profile"];
  option: string = this.navbarPaths[1];

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
