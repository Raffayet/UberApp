import { ToastrService } from 'ngx-toastr';
import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { SocialUser } from "angularx-social-login";
import { TokenUtilsService } from 'src/app/modules/shared/services/token-utils.service';
import { LoginService } from 'src/app/modules/auth/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  submitted = false;
  success = false;
  res = '';
  user: SocialUser;
  loggedIn: boolean;

  constructor(private loginService: LoginService, private router: Router, private tokenUtilsService: TokenUtilsService, private toastService: ToastrService) {}
  
  ngOnInit() {
      this.success = false;
      this.loginForm = new FormGroup({
          'email': new FormControl('', Validators.required),
          'password': new FormControl('', Validators.required)
      });
  }
  
  get formFields() { return this.loginForm.controls; }
  
  onSubmit() { 
      localStorage.clear();
      this.submitted = true;

        this.loginService.logIn(this.loginForm, this.success)
        // .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
        .subscribe({
          next: (res) => {
            let token = res.accessToken;
            localStorage.setItem("user", token);
            this.success = true;
            
            let role:string | null = this.tokenUtilsService.getRoleFromToken();
            this.toastService.success("Successfully logged in!");  
            switch(role){
              case 'CLIENT':
                this.router.navigate(['/client', {outlets: {'ClientRouter': ['request-ride-page']}}]);
                break;
              case 'DRIVER':
                this.router.navigate(['/driver', {outlets: {'DriverRouter': ['profile-page']}}]);
                break;
              case 'ADMIN':
                this.router.navigate(['/admin', {outlets: {'AdminRouter': ['homepage']}}]);
                break;
              default:
                this.router.navigateByUrl('/login');
            }
          },
          error: (err) => {                    
            if(err === "OK"){
              this.toastService.warning("Credentials are poorly formatted!");  
            }else{
              this.toastService.warning(err);  
            }
            this.success = false;
          },
        });
  }
}
