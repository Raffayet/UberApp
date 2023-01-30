import { CommonModule } from '@angular/common';
import { NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule, By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { ToastrService } from 'ngx-toastr';
import { of, throwError } from 'rxjs';
import { RegisterService } from '../../services/register.service';
import { RegistrationPageComponent } from './registration-page.component';

describe('RegistrationPageComponent', () => {
  let component: RegistrationPageComponent;
  let fixture: ComponentFixture<RegistrationPageComponent>;
  const registerServiceSpy = jasmine.createSpyObj<RegisterService>(['register']);
  const routerSpy = jasmine.createSpyObj<Router>(['navigate', 'navigateByUrl']);
  const toastSpy = jasmine.createSpyObj<ToastrService>(['success', 'error']);

  beforeEach( () => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        RouterTestingModule.withRoutes([
          {path: 'registration', component: RegistrationPageComponent }
        ]),
      ],
      providers: [
        { provide: RegisterService, useValue: registerServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ToastrService, useValue: toastSpy }
      ],
      declarations: [ RegistrationPageComponent ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  })

  beforeEach(() => {
    fixture = TestBed.createComponent(RegistrationPageComponent);
    component = fixture.debugElement.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("should disable next button when all fields are empty", () => {
    fixture.detectChanges();
    component.form.patchValue({
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
    
    component.form.patchValue({
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
    
    component.form.patchValue({
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
    
    component.form.patchValue({
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

  it("should disable next button when email field has wrong email format", () => {
    fixture.detectChanges();
    
    component.form.patchValue({
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

  it("should success registration form", () => {
    fixture.detectChanges();
    
    component.form.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.buttonDiv button'));
    expect(!button.nativeElement.disabled).toBeTruthy();
  });

  it("should success register process", () => {
    fixture.detectChanges();
    
    component.form.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();

    routerSpy.navigate.calls.reset();
    registerServiceSpy.register.and.returnValue(of("Success!"));
    fixture.detectChanges();
    
    expect(component.form.valid).toBeTruthy();
    component.onSubmit();
    
    expect(registerServiceSpy.register).toHaveBeenCalled();
    expect(routerSpy.navigateByUrl).toHaveBeenCalled();
  });

  it("should throw error while registering", () => {
    fixture.detectChanges();
    
    component.form.patchValue({
      firstName: "Pera",
      lastName: "Peric",
      email:"pera@gmail.com",
      password:"Perica123",
      confirmPassword:"Perica123",
      city:"Novi Sad",
      telephone:"0655551111"
    });
    
    fixture.detectChanges();

    registerServiceSpy.register.and.returnValue(throwError(() => new Error("FAILED!")));
    fixture.detectChanges();
    
    expect(component.form.valid).toBeTruthy();
    component.onSubmit();
    
    expect(registerServiceSpy.register).toHaveBeenCalled();
  });

});
