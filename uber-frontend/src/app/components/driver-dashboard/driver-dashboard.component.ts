import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-driver-dashboard',
  templateUrl: './driver-dashboard.component.html',
  styleUrls: ['./driver-dashboard.component.css']
})
export class DriverDashboardComponent {
  constructor(private router: Router){}

  ngOnInit() {
      
  }

  logout = () => {
    this.router.navigateByUrl('/login');
  }
}
