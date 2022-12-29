import { Component } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { DriverRegister } from 'src/app/model/DriverRegister';
import { identityRevealedValidator } from 'src/app/modules/auth/pages/registration-page/registration-page.component';
import { RegisterService } from 'src/app/modules/auth/services/register.service';
import { DriverService } from '../../services/driver.service';

@Component({
  selector: 'app-register-driver',
  templateUrl: './register-driver.component.html',
  styleUrls: ['./register-driver.component.css']
})
export class RegisterDriverComponent {
  firstFormGroup:FormGroup
  secondFormGroup:FormGroup
  vehicleTypeOptions:string[] = []
  
  
  constructor(private registerService: RegisterService, private driverService:DriverService, private router:Router){
  }
  
 
  ngOnInit(): void {
    this.firstFormGroup = new FormGroup({
      firstName: new FormControl('',[Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email:new FormControl('', [Validators.required, Validators.email]),
      password:new FormControl('', [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}")]),
      confirmPassword:new FormControl('', [Validators.required]),
      city:new FormControl('', [Validators.required]),
      telephone:new FormControl('', [Validators.required])
    }, { validators: identityRevealedValidator });

    this.secondFormGroup = new  FormGroup({
      model:new FormControl('',[Validators.required]),
      vehicleType:new FormControl('',[Validators.required]),
    });

    this.loadVehicleTypeOptions();
    
  }

  loadVehicleTypeOptions(){
    this.driverService.getVehicleTypes()
        .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
        .subscribe({
          next: (res:string[]) => {
            this.vehicleTypeOptions = res;
          },
          error: (err) => {
            this.vehicleTypeOptions = []
            console.log("Greska")
          },
        });
  }

  get firstName(){
    return this.firstFormGroup.get("firstName");
  }
  get lastName(){
    return this.firstFormGroup.get("lastName");
  }
  get email(){
    return this.firstFormGroup.get("email");
  }
  get password(){
    return this.firstFormGroup.get("password");
  }
  get confirmPassword(){
    return this.firstFormGroup.get("confirmPassword");
  }
  get city(){
    return this.firstFormGroup.get("city");
  }
  get telephone(){
    return this.firstFormGroup.get("telephone");
  }

  get model(){
    return this.secondFormGroup.get("model");
  }

  get vehicleType(){
    return this.secondFormGroup.get("vehicleType");
  }

  registerDriver(){
    let registerDto:DriverRegister = this.firstFormGroup.value;
    registerDto.vehicle = this.secondFormGroup.value;
    console.log(registerDto);

    this.registerService.registerDriver(registerDto)
      .subscribe({
        next: () => {
          console.log("Uspeh")
          this.router.navigateByUrl("/admin");
        },
        error: (err) => {
          console.log("Greskaa");
          console.log(err);
        }
      });
  }

}
