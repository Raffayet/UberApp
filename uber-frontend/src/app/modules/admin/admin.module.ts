import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { RegisterDriverComponent } from './pages/register-driver/register-driver.component';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';


@NgModule({
  declarations: [
    AdminDashboardComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MaterialComponentsModule
  ]
})
export class AdminModule { }
