import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { RegistrationPageComponent } from './components/registration-page/registration-page.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'home-page', component: HomePageComponent },
  { path: 'registration', component: RegistrationPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
