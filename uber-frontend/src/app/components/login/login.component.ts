import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { catchError, lastValueFrom, throwError } from 'rxjs';
import { LoginService } from 'src/app/services/login.service';
import { Router } from '@angular/router';
import { SocialAuthService } from "@abacritt/angularx-social-login";
import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import { SocialUser } from "angularx-social-login";
import { GoogleSigninButtonDirective } from '@abacritt/angularx-social-login';

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

  constructor(private loginService: LoginService, private router: Router, private authService: SocialAuthService) {}
  
  ngOnInit() {
      this.loginForm = new FormGroup({
          'email': new FormControl('', Validators.required),
          'password': new FormControl('', Validators.required)
      });
      this.authService.authState.subscribe((user) => {
        this.user = user;
        this.loggedIn = (user != null);
      });
      // @ts-ignore
      google.accounts.id.initialize({
        client_id: '942553161348-dq7es99f69eovl0q1vl98ilgncia59nq.apps.googleusercontent.com',
        callback: this.handleCredentialResponse.bind(this),
        auto_select: false,
        cancel_on_tap_outside: true,

      });
      // @ts-ignore
      google.accounts.id.renderButton(
      // @ts-ignore
      document.getElementById("google-button"),
        { theme: "outline", size: "large", width: "100%"}
      );
      // @ts-ignore
      google.accounts.id.prompt((notification: PromptMomentNotification) => {})
  }
  
  get formFields() { return this.loginForm.controls; }

  onSubmit() { 
      this.submitted = true;
      this.success = true;
        this.loginService.logIn(this.loginForm, this.success)
        .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
        .subscribe({
          next: (res) => {
            console.log('uspesno');
            this.success = true;
            this.router.navigateByUrl(`/dashboard/${this.loginForm.value.email}`);
          },
          error: (err) => {
            this.success = false;
          },
        });
      console.log(this.success)
      // let data = await lastValueFrom(response);
      // console.log(data)
  }
  
  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
    if(this.user)
    {
      this.router.navigateByUrl(`/dashboard/${this.user.email}`);
    }
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID);
    // this.router.navigateByUrl(`/dashboard/${this.user.email}`);
  }

  signOut(): void {
    this.authService.signOut();
  }

  refreshToken(): void {
    this.authService.refreshAuthToken(GoogleLoginProvider.PROVIDER_ID);
  }

  async handleCredentialResponse(response: any) {
    // Here will be your response from Google.
    this.signInWithGoogle()
  }
}
