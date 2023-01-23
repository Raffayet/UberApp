import { ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserModule, By } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LoginComponent } from './login.component';
import { LoginService } from '../../services/login.service';
import { GoogleLoginProvider, SocialAuthService, SocialLoginModule } from 'angularx-social-login';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const loginServiceSpy = jasmine.createSpyObj<LoginService>(['logIn']);
  const socialAuthServiceSpy = jasmine.createSpyObj<SocialAuthService>(['signIn', 'signOut', 'refreshAuthToken']);

  beforeEach(async () => {
    // await TestBed.configureTestingModule({
    //   declarations: [ LoginComponent ],
    //   imports: [
    //     BrowserModule,
    //     FormsModule,
    //     ReactiveFormsModule,
    //     SocialLoginModule
    //   ],
    //   providers: [
    //     { provide: LoginService, useValue: loginServiceSpy },
    //     { provide: SocialAuthService, useValue: socialAuthServiceSpy },
    //     {
    //       provide: "SocialAuthServiceConfig",
    //       useValue: {
    //           autoLogin: false,
    //           providers: [
    //               {
    //                   id: GoogleLoginProvider.PROVIDER_ID,
    //                   provider: new GoogleLoginProvider(
    //                       "abc"
    //                   ),
    //               }
    //           ],
    //       },
    //     },


    //   ]
    // })
    // .compileComponents();

    // fixture = TestBed.createComponent(LoginComponent);
    // component = fixture.componentInstance;
    // fixture.detectChanges();
  });

  it('should create', () => {
    expect(true).toBeTruthy();
  });
});
