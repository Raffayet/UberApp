import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  email: string | null;

  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void{
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.email = params.get("email");
    });
  }

  socialSignOut(): void{
    this.router.navigateByUrl('/login');
  }
}
