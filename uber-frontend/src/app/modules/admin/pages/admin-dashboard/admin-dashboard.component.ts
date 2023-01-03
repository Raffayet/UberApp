import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/modules/shared/services/user.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  chatHidden:boolean = false;

  constructor(private router: Router, private userService:UserService){}

  ngOnInit() {
    
  }

  LogOut(){
    this.userService.logOut();
  }

}
