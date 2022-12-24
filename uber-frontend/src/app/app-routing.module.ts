import { RegistrationPageComponent } from './modules/auth/pages/registration-page/registration-page.component';
import { RegisteredAccountPageComponent } from './modules/auth/pages/registered-account-page/registered-account-page.component';
import { ActivatedAccountComponent } from './modules/auth/pages/activated-account/activated-account.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './modules/auth/pages/login/login.component';
import { RideRequestPageComponent } from './modules/client/pages/ride-request-page/ride-request-page.component';
import { LivechatComponent } from './modules/shared/components/livechat/livechat.component';
import { DriverDashboardComponent } from './modules/driver/pages/driver-dashboard/driver-dashboard.component';
import { AdminDashboardComponent } from './modules/admin/pages/admin-dashboard/admin-dashboard.component';
import { RegisteredSocialAccountComponent } from './modules/auth/pages/registered-social-account/registered-social-account.component';
import { AdditionalLoginInfoComponent } from './modules/auth/components/additional-login-info/additional-login-info.component';
import { UnauthenticatedDashboard } from './modules/auth/pages/unauthenticated-dashboard/unauthenticated-dashboard.component';
import { UserProfilePageComponent } from './modules/shared/pages/user-profile-page/user-profile-page.component';
import { ClientDashboardComponent } from './modules/client/pages/client-dashboard/client-dashboard.component';

const routes: Routes = [
  { path: '', component: UnauthenticatedDashboard},
  { path: 'login', component: LoginComponent },
  { path: 'home-page', component: RideRequestPageComponent }, 
  { path: 'admin-dashboard', component: AdminDashboardComponent },
  { path: 'registration', component: RegistrationPageComponent },
  { path: 'chat', component: LivechatComponent },
  { path: 'activatedAccount', component: ActivatedAccountComponent },
  { path: 'registeredAccount', component: RegisteredAccountPageComponent },
  { path: 'additionalLoginInfo', component: AdditionalLoginInfoComponent},
  { path: 'authenticatedSocialAccount', component: RegisteredSocialAccountComponent},
  {
    path: 'shared',
    loadChildren: () => import('./modules/shared/shared.module').then(m => m.SharedModule)
  },
  {
    path: 'client',
    component: ClientDashboardComponent,
    loadChildren: () => import('./modules/client/client.module').then(m => m.ClientModule)
  },
  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.module').then(m => m.AdminModule)
  },
  {
    path: 'driver',
    component: DriverDashboardComponent,
    loadChildren: () => import('./modules/driver/driver.module').then(m => m.DriverModule)
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { anchorScrolling: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
