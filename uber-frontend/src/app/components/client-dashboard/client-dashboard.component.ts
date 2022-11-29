import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

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
