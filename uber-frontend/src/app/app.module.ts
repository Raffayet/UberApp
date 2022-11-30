import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoginComponent } from './components/login/login.component';
import { RideRequestPageComponent } from './components/ride-request-page/ride-request-page.component';
import {PasswordModule} from 'primeng/password';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import { RegistrationPageComponent } from './components/registration-page/registration-page.component';
import { MatListModule } from '@angular/material/list';
import { MapComponent } from './components/map/map.component';
import {MatDividerModule} from '@angular/material/divider';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { GeocodingComponent } from './components/map/geocoding/geocoding.component';
import { LivechatComponent } from './components/livechat/livechat.component';
import { MatIconModule } from '@angular/material/icon';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './helpers/jwt-interceptor';
import { ActivatedAccountComponent } from './components/activated-account/activated-account.component';
import { RegisteredAccountPageComponent } from './components/registered-account-page/registered-account-page.component';
import { ClientDashboardComponent } from './components/client-dashboard/client-dashboard.component';
import { DriverDashboardComponent } from './components/driver-dashboard/driver-dashboard.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ToastrModule } from 'ngx-toastr';
import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider
} from '@abacritt/angularx-social-login';
import { AdditionalLoginInfoComponent } from './components/additional-login-info/additional-login-info.component';
import { RegisteredSocialAccountComponent } from './components/registered-social-account/registered-social-account.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RideRequestPageComponent,
    RegistrationPageComponent,
    MapComponent,
    GeocodingComponent,
    LivechatComponent,
    ActivatedAccountComponent,
    RegisteredAccountPageComponent,
    ClientDashboardComponent,
    DriverDashboardComponent,
    AdminDashboardComponent,
    AdditionalLoginInfoComponent,
    RegisteredSocialAccountComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    PasswordModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserAnimationsModule,
    MatSlideToggleModule,
    MatButtonModule,
    MatInputModule,
    MatDividerModule,
    MatAutocompleteModule,
    MatAutocompleteModule,
    MatListModule,
    MatIconModule,
    SocialLoginModule,
    DragDropModule,
    ToastrModule.forRoot({
      timeOut: 2000,
      positionClass: 'toast-bottom-right',
    }),
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
