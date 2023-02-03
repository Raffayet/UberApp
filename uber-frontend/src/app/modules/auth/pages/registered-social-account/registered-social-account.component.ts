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
    this.loginService.socialLogIn(String(this.route.snapshot.paramMap.get("email")))
    .subscribe({
      next: (res) => {
        
        localStorage.setItem("user", res.accessToken);
        this.router.navigate(['/client', {outlets: {'ClientRouter': ['request-ride-page']}}]);
      },
      error: (err) => {
        console.log("Greskaa");
        console.log(err);
      }
    });
  }
}
