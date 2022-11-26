import { Component } from '@angular/core';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;

    constructor(){}

    ngOnInit() {
      
    }

    toggleChat = () => {
      this.chatHidden = !this.chatHidden;
    }
}
