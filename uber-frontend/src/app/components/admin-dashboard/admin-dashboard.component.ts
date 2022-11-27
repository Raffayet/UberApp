import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {
  chatHidden:boolean = false;

  constructor(){}

  ngOnInit() {
    
  }

  toggleChat = () => {
    this.chatHidden = !this.chatHidden;
  }
}
