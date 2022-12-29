import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterDriverComponent } from './pages/register-driver/register-driver.component';

const routes: Routes = [
  {
    path: 'register-driver',
    component: RegisterDriverComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
