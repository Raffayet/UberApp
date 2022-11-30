import { RegistrationPageComponent } from './components/registration-page/registration-page.component';
import { RegisteredAccountPageComponent } from './components/registered-account-page/registered-account-page.component';
import { ActivatedAccountComponent } from './components/activated-account/activated-account.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RideRequestPageComponent } from './components/ride-request-page/ride-request-page.component';
import { LivechatComponent } from './components/livechat/livechat.component';
import { ClientDashboardComponent } from './components/client-dashboard/client-dashboard.component';
import { DriverDashboardComponent } from './components/driver-dashboard/driver-dashboard.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { RegisteredSocialAccountComponent } from './components/registered-social-account/registered-social-account.component';
import { AdditionalLoginInfoComponent } from './components/additional-login-info/additional-login-info.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home-page', component: RideRequestPageComponent }, 
  { path: 'client-dashboard', component: ClientDashboardComponent },
  { path: 'driver-dashboard', component: DriverDashboardComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent },
  { path: 'registration', component: RegistrationPageComponent },
  { path: 'chat', component: LivechatComponent },
  { path: 'activatedAccount', component: ActivatedAccountComponent },
  { path: 'registeredAccount', component: RegisteredAccountPageComponent },
  { path: 'additionalLoginInfo', component: AdditionalLoginInfoComponent},
  { path: 'authenticatedSocialAccount', component: RegisteredSocialAccountComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
