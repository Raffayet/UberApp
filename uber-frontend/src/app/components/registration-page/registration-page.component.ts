
import { Router } from '@angular/router';
import { Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators, AbstractControl, ValidatorFn, ValidationErrors} from '@angular/forms';
import { RegisterService } from 'src/app/services/register.service';
import { Register } from 'src/app/model/register';


export const identityRevealedValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const confirmPassword = control.get('confirmPassword');

  if(password && confirmPassword && password.value !== confirmPassword.value){
    confirmPassword.setErrors({notEquivalent: true});
  }
  else if(password && confirmPassword && !confirmPassword.errors?.['required']){
    confirmPassword.setErrors(null);
  }

  return password && confirmPassword && password.value !== confirmPassword.value ? { notMatchingPasswords: true } : null;
};

@Component({
  selector: 'app-registration-page',
  templateUrl: './registration-page.component.html',
  styleUrls: ['./registration-page.component.css']
})
export class RegistrationPageComponent implements OnInit {

  form:FormGroup 

  constructor(private registerService: RegisterService, private router:Router){
  }

  ngOnInit(){
    this.form = new FormGroup({
      firstName: new FormControl('',[Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email:new FormControl('', [Validators.required, Validators.email]),
      password:new FormControl('', [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}")]),
      confirmPassword:new FormControl('', [Validators.required]),
      city:new FormControl('', [Validators.required]),
      telephone:new FormControl('', [Validators.required])
    }, { validators: identityRevealedValidator })
  }

  get firstName(){
    return this.form.get("firstName");
  }
  get lastName(){
    return this.form.get("lastName");
  }
  get email(){
    return this.form.get("email");
  }
  get password(){
    return this.form.get("password");
  }
  get confirmPassword(){
    return this.form.get("confirmPassword");
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
    localStorage.clear();
    console.log(this.form.errors);
    console.log(this.form.invalid);
    if(!this.form.invalid){
      let registerDto:Register = this.form.value;
      registerDto.provider = "LOCAL";
      console.log("DTO REGISTRACIJE:")
      console.log(registerDto);

      this.registerService.register(registerDto)
        .subscribe({
          next: () => {
            console.log("USpeh")
            this.router.navigateByUrl("/registeredAccount");
          },
          error: (err) => {
            console.log("Greskaa");
            console.log(err);
          }
        });

      
    }
  }
}
