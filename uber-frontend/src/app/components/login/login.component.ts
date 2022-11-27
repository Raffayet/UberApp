import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { catchError, lastValueFrom, throwError } from 'rxjs';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import { TokenUtilsService } from 'src/app/services/token-utils.service';

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

  constructor(private loginService: LoginService, private router: Router, private tokenUtilsService: TokenUtilsService) {}
  
  ngOnInit() {
      this.loginForm = new FormGroup({
          'email': new FormControl('', Validators.required),
          'password': new FormControl('', Validators.required)
      });
  }
  
  get formFields() { return this.loginForm.controls; }

  onSubmit() { 
      this.submitted = true;

        this.loginService.logIn(this.loginForm, this.success)
        .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
        .subscribe({
          next: (res) => {
            let token = res.accessToken;
            localStorage.setItem("user", token);
            this.success = true;
            let role:string | null = this.tokenUtilsService.getRoleFromToken();
            switch(role){
              case 'CLIENT':
                this.router.navigateByUrl('/client-dashboard');
                break;
              case 'DRIVER':
                this.router.navigateByUrl('/driver-dashboard');
                break;
              case 'ADMIN':
                this.router.navigateByUrl('/admin-dashboard');
                break;
              default:
                this.router.navigateByUrl('/login');
            }
            
          },
          error: (err) => {
            this.success = false;
          },
        });

      // let data = await lastValueFrom(response);
      // console.log(data)
  }
  
  recognizeGoogleAccount(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  goToRegistration():void{
    this.router.navigateByUrl('/registration');
  }

  recognizeFacebookAccount(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID);
  }

}
