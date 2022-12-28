import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './modules/auth/pages/login/login.component';
import {PasswordModule} from 'primeng/password';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationPageComponent } from './modules/auth/pages/registration-page/registration-page.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './helpers/jwt-interceptor';
import { ActivatedAccountComponent } from './modules/auth/pages/activated-account/activated-account.component';
import { RegisteredAccountPageComponent } from './modules/auth/pages/registered-account-page/registered-account-page.component';
import { ClientDashboardComponent } from './modules/client/pages/client-dashboard/client-dashboard.component';
import { DriverDashboardComponent } from './modules/driver/pages/driver-dashboard/driver-dashboard.component';
import { AdminDashboardComponent } from './modules/admin/pages/admin-dashboard/admin-dashboard.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ToastrModule } from 'ngx-toastr';
import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider
} from '@abacritt/angularx-social-login';
import { AdditionalLoginInfoComponent } from './modules/auth/components/additional-login-info/additional-login-info.component';
import { RegisteredSocialAccountComponent } from './modules/auth/pages/registered-social-account/registered-social-account.component';
import { UnauthenticatedDashboard } from './modules/auth/pages/unauthenticated-dashboard/unauthenticated-dashboard.component';
import { UserProfilePageComponent } from './modules/shared/pages/user-profile-page/user-profile-page.component';
import { MaterialComponentsModule } from './modules/material-components/material-components.module';
import { SharedModule } from './modules/shared/shared.module';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationPageComponent,
    ActivatedAccountComponent,
    RegisteredAccountPageComponent,
    ClientDashboardComponent,
    AdminDashboardComponent,
    AdditionalLoginInfoComponent,
    RegisteredSocialAccountComponent,
    UnauthenticatedDashboard,
    UserProfilePageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    PasswordModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    SocialLoginModule,
    // DragDropModule,   
    MaterialComponentsModule,
    ToastrModule.forRoot({
      timeOut: 2000,
      positionClass: 'toast-bottom-right',
    }),
    SharedModule,
  ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '942553161348-dq7es99f69eovl0q1vl98ilgncia59nq.apps.googleusercontent.com'
            )
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('726405335024718')
          },
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }

  ], 
  bootstrap: [AppComponent]
})
export class AppModule { }
