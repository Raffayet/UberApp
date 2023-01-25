import { SocialLoginComponent } from './../../components/social-login/social-login.component';
import { HttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { LoginComponent } from './login.component';
import { LoginService } from '../../services/login.service';
import { Observable, of, throwError } from 'rxjs';
import { By } from "@angular/platform-browser";
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { tokenResponse, loginForm } from '../../mocks/login';
import { fakeAsync, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { RideRequestPageComponent } from 'src/app/modules/client/pages/ride-request-page/ride-request-page.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let router: Router;

  const loginServiceSpy = jasmine.createSpyObj<LoginService>(['logIn']);
  let routerSpy = jasmine.createSpyObj<Router>(['navigate', 'navigateByUrl']);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        RouterTestingModule.withRoutes([
          {path: 'login', component: RideRequestPageComponent }
        ]),
      ],
      providers: [
        { provide: LoginService, useValue: loginServiceSpy },
        { provide: Router, useValue: routerSpy }
      ],
      declarations: [LoginComponent],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.debugElement.componentInstance;
    // router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(true).toBeTruthy();
  });

  it("should disable button when username and password are empty", () => {
    fixture.detectChanges();
    
    component.loginForm.patchValue({
      email: "",
      password: ""
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.login-button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });

  it("should disable button when username is empty", () => {
    fixture.detectChanges();
    
    component.loginForm.patchValue({
      email: "",
      password: "jovan123"
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.login-button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });

  it("should disable button when password is empty", () => {
    fixture.detectChanges();

    component.loginForm.patchValue({
      email: "jovancevic@gmail.com",
      password: ""
    });
    
    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.login-button'));
    expect(button.nativeElement.disabled).toBeTruthy();
  });

  it("should enable button when email and password are filled", () => {
    fixture.detectChanges();

    component.loginForm.patchValue({
      email: "jovancevic@gmail.com",
      password: "jovan123"
    });

    fixture.detectChanges();
    const button = fixture.debugElement.query(By.css('.login-button'));
    
    expect(button.nativeElement.disabled).toBeFalsy();
  });

  it("should succeed login", fakeAsync(() => {
    routerSpy.navigate.calls.reset();
    loginServiceSpy.logIn.and.returnValue(of(tokenResponse));
    fixture.detectChanges();
    
    expect(component.success).toBeFalsy();
    component.onSubmit();
    
    expect(loginServiceSpy.logIn).toHaveBeenCalled();
    expect(component.success).toBeTruthy();
    expect(routerSpy.navigate).toHaveBeenCalled();

  }));

  it("should fail login", fakeAsync(() => {
    routerSpy.navigateByUrl.calls.reset();

    loginServiceSpy.logIn.and.returnValue(of("tokenResponse"));
    fixture.detectChanges();
  
    expect(component.success).toBeFalsy();
    component.onSubmit();
    
    expect(loginServiceSpy.logIn).toHaveBeenCalled();
    expect(component.success).toBeTruthy();
    expect(routerSpy.navigateByUrl).toHaveBeenCalled();

    const spy = routerSpy.navigateByUrl as jasmine.Spy;
    const navArgs = spy.calls.first().args[0];    
    expect(navArgs).toEqual('/login'); 

  }));

  it("should throw error login", fakeAsync(() => {
    routerSpy.navigate.calls.reset();
    routerSpy.navigateByUrl.calls.reset();

    loginServiceSpy.logIn.and.returnValue(throwError(() => new Error("Greska")));
    routerSpy.navigate.calls.reset();
    fixture.detectChanges();
    
    expect(component.success).toBeFalsy();
    component.onSubmit();

    expect(loginServiceSpy.logIn).toHaveBeenCalled();
    expect(component.success).toBeFalsy();

    expect(routerSpy.navigate.calls.count()).toEqual(0);
    expect(routerSpy.navigateByUrl.calls.count()).toEqual(0);
 
  }));

});
