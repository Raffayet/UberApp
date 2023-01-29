import { MatStepperModule } from '@angular/material/stepper';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RegisterService } from 'src/app/modules/auth/services/register.service';
import { DriverService } from 'src/app/modules/admin/services/driver.service';

import { RegisterDriverComponent } from './register-driver.component';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule, By } from '@angular/platform-browser';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { AdminDashboardComponent } from '../admin-dashboard/admin-dashboard.component';
import { Observable, of, throwError } from 'rxjs';
import { vehicleTypes } from '../../mocks/register-driver';
import { ToastrService } from 'ngx-toastr';

describe('RegisterDriverComponent', () => {
  let component: RegisterDriverComponent;
  let fixture: ComponentFixture<RegisterDriverComponent>;
  const registerServiceSpy = jasmine.createSpyObj<RegisterService>(['registerDriver']);
  const driverServiceSpy = jasmine.createSpyObj<DriverService>(['getVehicleTypes']);
  const routerSpy = jasmine.createSpyObj<Router>(['navigate', 'navigateByUrl']);
  const toastSpy = jasmine.createSpyObj<ToastrService>(['success', 'error']);

  beforeEach( () => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        RouterTestingModule.withRoutes([
          {path: 'register', component: AdminDashboardComponent }
        ]),
      ],
      providers: [
        { provide: RegisterService, useValue: registerServiceSpy },
        { provide: DriverService, useValue: driverServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ToastrService, useValue: toastSpy }
      ],
      declarations: [ RegisterDriverComponent ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterDriverComponent);
    component = fixture.debugElement.componentInstance;
    driverServiceSpy.getVehicleTypes.and.returnValue(of(vehicleTypes));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("should disable next button when all fields are empty", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "",
      lastName: "",
      email:"",
      password:"",
      confirmPassword:"",
      city:"",
      telephone:""
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.buttonDiv button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when one fields is empty", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"perica123",
      confirmPassword:"perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.buttonDiv button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when password is incorect", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"pe123",
      confirmPassword:"pe123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.buttonDiv button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });

  it("should disable next button when confirm password is not matching fields", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Pericaaa123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.buttonDiv button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when email field is not email", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"not email",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.buttonDiv button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should success first form", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'',
      vehicleType: ''
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#firstNext'));
    expect(!button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when second Form is empty", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'',
      vehicleType: ''
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#secondNext'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when second Form is empty", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'',
      vehicleType: ''
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#secondNext'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when model in second Form is empty", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'skoda octavia',
      vehicleType: ''
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#secondNext'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should disable next button when model in second Form is empty", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'skoda octavia',
      vehicleType: ''
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#secondNext'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should success second form", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'skoda octavia',
      vehicleType: 'Standard'
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#secondNext'));
    expect(!button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should success second form", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'skoda octavia',
      vehicleType: 'Standard'
    })
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('#secondNext'));
    expect(!button.nativeElement.disabled).toBeTruthy();
  });
  
  it("should success register", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'skoda octavia',
      vehicleType: 'Standard'
    })
    
    fixture.detectChanges();

    routerSpy.navigate.calls.reset();
    registerServiceSpy.registerDriver.and.returnValue(of("Success!"));
    fixture.detectChanges();
    
    expect(component.firstFormGroup.valid).toBeTruthy();
    expect(component.secondFormGroup.valid).toBeTruthy();
    component.registerDriver();
    
    expect(registerServiceSpy.registerDriver).toHaveBeenCalled();
    expect(toastSpy.success).toHaveBeenCalled();
    expect(routerSpy.navigateByUrl).toHaveBeenCalled();
  });
  
  it("should throw error while registering", () => {
    fixture.detectChanges();
    
    component.firstFormGroup.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });

    component.secondFormGroup.patchValue({
      model:'skoda octavia',
      vehicleType: 'Standard'
    })
    
    fixture.detectChanges();

    registerServiceSpy.registerDriver.and.returnValue(throwError(() => new Error("FAILED!")));
    fixture.detectChanges();
    
    expect(component.firstFormGroup.valid).toBeTruthy();
    expect(component.secondFormGroup.valid).toBeTruthy();
    component.registerDriver();
    
    expect(registerServiceSpy.registerDriver).toHaveBeenCalled();
    expect(toastSpy.error).toHaveBeenCalled();
  });


});
