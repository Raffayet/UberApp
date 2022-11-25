import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  email: string | null;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void{
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.email = params.get("email");
    });
  }
}
