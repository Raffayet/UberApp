import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon'
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { LoginService } from 'src/app/modules/auth/services/login.service';

@Component({
  selector: 'app-registered-social-account',
  templateUrl: './registered-social-account.component.html',
  styleUrls: ['./registered-social-account.component.css']
})
export class RegisteredSocialAccountComponent {
  constructor(private router:Router, private loginService: LoginService, private route: ActivatedRoute){}

  goToClientDashboard(){
    console.log(this.route.snapshot.paramMap.get("email"))
    this.loginService.socialLogIn(String(this.route.snapshot.paramMap.get("email")))
    this.router.navigateByUrl('/client-dashboard');
  }
}
