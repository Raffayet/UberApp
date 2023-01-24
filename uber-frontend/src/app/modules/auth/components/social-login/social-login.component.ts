import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SocialAuthService } from "@abacritt/angularx-social-login";
import { FacebookLoginProvider, GoogleLoginProvider } from "angularx-social-login";
import { SocialUser } from "angularx-social-login";
import { SocialUserInfo } from 'src/app/model/SocialUserInfo';

@Component({
  selector: 'app-social-login',
  templateUrl: './social-login.component.html',
  styleUrls: ['./social-login.component.css']
})
export class SocialLoginComponent implements OnInit{

  user: SocialUser;
  loggedIn: boolean;
  
  constructor(private router: Router, private authService: SocialAuthService) {}

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);
    });
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
    let socialUserDto: SocialUserInfo = this.user;
    let route = 'additionalLoginInfo';
    
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
