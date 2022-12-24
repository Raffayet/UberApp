import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { catchError, lastValueFrom, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { SocialAuthService } from "@abacritt/angularx-social-login";
import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import { SocialUser } from "angularx-social-login";
import { SocialUserInfo } from 'src/app/model/SocialUserInfo';
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

  constructor(private loginService: LoginService, private router: Router, private tokenUtilsService: TokenUtilsService, private authService: SocialAuthService) {}
  
  ngOnInit() {
      this.success = true;
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
                this.router.navigate(['/client', {outlets: {'ClientRouter': ['request-ride-page']}}]);
                break;
              case 'DRIVER':
                this.router.navigate(['/driver', {outlets: {'DriverRouter': ['profile-page']}}]);
                break;
              case 'ADMIN':
                this.router.navigateByUrl('/admin');
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
    let socialUserDto: SocialUserInfo = this.user
    let route = 'additionalLoginInfo'
    this.router.navigate([route, {
      email: socialUserDto.email,
      id: socialUserDto.id,
      idToken: socialUserDto.idToken,
      firstName: socialUserDto.firstName,
      lastName: socialUserDto.lastName,
      name: socialUserDto.name,
      photoUrl: socialUserDto.photoUrl,
      provider: socialUserDto.provider
    }]);
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
