import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  chatHidden:boolean = false;

  constructor(private router: Router){}

  ngOnInit() {
    
  }

  toggleChat = () => {
    this.chatHidden = !this.chatHidden;
  }

  logout = () => {
    this.router.navigateByUrl('/login');
  }
}
