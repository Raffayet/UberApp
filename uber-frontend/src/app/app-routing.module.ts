import { RegisteredAccountPageComponent } from './components/registered-account-page/registered-account-page.component';
import { ActivatedAccountComponent } from './components/activated-account/activated-account.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { RegistrationPageComponent } from './components/registration-page/registration-page.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { LivechatComponent } from './components/livechat/livechat.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home-page', component: HomePageComponent },
  { path: 'registration', component: RegistrationPageComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'chat', component: LivechatComponent },
  { path: 'activatedAccount', component: ActivatedAccountComponent },
  { path: 'registeredAccount', component: RegisteredAccountPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
