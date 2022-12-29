import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { UserService } from 'src/app/modules/shared/services/user.service';

@Component({
  selector: 'app-driver-dashboard',
  templateUrl: './driver-dashboard.component.html',
  styleUrls: ['./driver-dashboard.component.css']
})
export class DriverDashboardComponent {

  chatHidden:boolean = false;
  navbarLabels: string[] = ["Profile"];
  navbarPaths: string[] = ["profile-page"];
  option: string = this.navbarPaths[0];

  changeOption(eventData: string): void{      
    this.router.navigate(['/driver', {outlets: {'DriverRouter': ['profile-page']}}]);
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