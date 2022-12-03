import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-client-dashboard',
  templateUrl: './client-dashboard.component.html',
  styleUrls: ['./client-dashboard.component.css']
})
export class ClientDashboardComponent {

    chatHidden:boolean = false;
    navbarLabels: string[] = ["Home", "Profile"];
    navbarPaths: string[] = ["/client-dashboard", "/user-profile"];
    option: string = this.navbarPaths[1];

    changeOption(eventData: string): void{      
      this.option = eventData;
    }

    constructor(private router: Router){}

    ngOnInit() {}

    toggleChat = () => {
      this.chatHidden = !this.chatHidden;
    }

    logout = () => {
      localStorage.removeItem("user");
      this.router.navigateByUrl('/');
    }
}
