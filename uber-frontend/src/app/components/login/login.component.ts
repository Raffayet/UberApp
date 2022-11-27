import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { catchError, lastValueFrom, throwError } from 'rxjs';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import { TokenUtilsService } from 'src/app/services/token-utils.service';
import { SocialAuthService } from "@abacritt/angularx-social-login";
import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import { SocialUser } from "angularx-social-login";

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

  constructor(private loginService: LoginService, private router: Router, private tokenUtilsService: TokenUtilsService, private authService: SocialAuthService) {}
  
  ngOnInit() {
      this.loginForm = new FormGroup({
          'email': new FormControl('', Validators.required),
          'password': new FormControl('', Validators.required)
      });
      this.authService.authState.subscribe((user) => {
        this.user = user;
        this.loggedIn = (user != null);
      });
  }
  
  get formFields() { return this.loginForm.controls; }
  
  onSubmit() { 
      localStorage.clear();
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

  signIn(): void {
    console.log(this.user)
    // if(this.user)
    //   this.router.navigateByUrl(`/dashboard`);
  }

  signOut(): void {
    this.authService.signOut();
  }

  refreshToken(): void {
    this.authService.refreshAuthToken(GoogleLoginProvider.PROVIDER_ID);
  }

  async handleCredentialResponse(response: any) {
    // Here will be your response from Google.
    this.recognizeGoogleAccount()
  }
}
