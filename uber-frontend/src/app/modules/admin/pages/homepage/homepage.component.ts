import { Router } from '@angular/router';
import { UserService } from './../../../shared/services/user.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent {

  navbarItems = [
    {
      label:'Register Driver',
      icon: 'person_add',
      router: 'register-driver'
    },{
      label:'Info Change Request',
      icon: 'data_usage',
      router: 'driver-info-request'
    },{
      label:'Report',
      icon: 'bar_chart',
      router: 'report'
    },{
      label:'Live Chat',
      icon: 'chat',
      router: 'livechat'
    },{
      label:'Block Users ',
      icon: 'block',
      router: 'block-users'
    },{
      label:'Ride History',
      icon: 'history',
      router: 'history'
    },
  ];

  constructor(private userService:UserService, private router:Router){}


  goToPage(item: { router: string; }){
    if(item.router === 'logout'){
      this.userService.logOut();
    }
    else 
      this.router.navigate(['/admin', {outlets: {'AdminRouter': [item.router]}}])
  }
}
