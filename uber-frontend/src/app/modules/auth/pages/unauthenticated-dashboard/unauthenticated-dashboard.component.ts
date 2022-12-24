import { Component } from '@angular/core';

@Component({
  selector: 'app-unauthenticated-dashboard',
  templateUrl: './unauthenticated-dashboard.component.html',
  styleUrls: ['./unauthenticated-dashboard.component.css']
})
export class UnauthenticatedDashboard {

    navbarLabels: string[] = ["Home", "Log In", "Register"];
    navbarPaths: string[] = ["/", "/login", "/registration"];
    option: string = this.navbarPaths[0];

    changeOption(eventData: string): void{
      this.option = eventData;
    }

}
