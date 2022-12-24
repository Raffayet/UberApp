import { Component, OnInit } from '@angular/core';
import { Data, Router } from '@angular/router';
import { AdditionalLoginInfo } from 'src/app/model/AdditionalLoginInfo';
import { FormControl, FormGroup, Validators, AbstractControl, ValidatorFn, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, ParamMap } from '@angular/router'
import { SocialUser } from 'angularx-social-login';
import { SocialUserInfo } from 'src/app/model/SocialUserInfo';
import { Navigation } from '@angular/router';
import { SocialLoginData } from 'src/app/model/SocialLoginData';
import { AdditionalLoginInfoService } from 'src/app/modules/auth/services/additional-login-info.service';

@Component({
  selector: 'app-additional-login-info',
  templateUrl: './additional-login-info.component.html',
  styleUrls: ['./additional-login-info.component.css']
})
export class AdditionalLoginInfoComponent {

  form: FormGroup 
  socialUserInfo: SocialUserInfo
  
  constructor(private additionalLoginInfoService: AdditionalLoginInfoService, private router:Router, private route: ActivatedRoute){
  }

  ngOnInit(){
    this.form = new FormGroup({
      city:new FormControl('', [Validators.required]),
      telephone:new FormControl('', [Validators.required])
    })
  }

  get city(){
    return this.form.get("city");
  }
  get telephone(){
    return this.form.get("telephone");
  }

  goToLogin(){
    this.router.navigateByUrl('/login');
  }

  onSubmit(){

    let additionalInfoDto: AdditionalLoginInfo = this.form.value;
    let socialLoginData: SocialLoginData= {
      idToken: String(this.route.snapshot.paramMap.get("idToken")),
      email: String(this.route.snapshot.paramMap.get("email")),
      firstName: String(this.route.snapshot.paramMap.get("firstName")),
      lastName: String(this.route.snapshot.paramMap.get("lastName")),
      name: String(this.route.snapshot.paramMap.get("name")),
      photoUrl: String(this.route.snapshot.paramMap.get("photoUrl")), 
      provider: String(this.route.snapshot.paramMap.get("provider")),
      city: String(this.form.value.city),
      telephone: String(this.form.value.telephone)
    }

    localStorage.clear();
    console.log(this.form.errors);
    console.log(this.form.invalid);
    if(!this.form.invalid){

      this.additionalLoginInfoService.loginSocialUser(socialLoginData)
        .subscribe({
          next: () => {
            console.log("USpeh")
            this.router.navigate(['authenticatedSocialAccount', {
              email: this.route.snapshot.paramMap.get("email")}]);
          },
          error: (err) => {
            console.log("Greskaa");
            console.log(err);
          }
        });
    }
  }
}
