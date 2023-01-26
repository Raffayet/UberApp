import { HomepageComponent } from './pages/homepage/homepage.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { RegisterDriverComponent } from './pages/register-driver/register-driver.component';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BlockUsersComponent } from './pages/block-users/block-users.component';
import { DriverInfoRequestComponent } from './pages/driver-info-request/driver-info-request.component';
import { DriverInfoDialogComponent } from './components/driver-info-dialog/driver-info-dialog.component';


@NgModule({
  declarations: [
    RegisterDriverComponent,
    BlockUsersComponent,
    HomepageComponent,
    DriverInfoRequestComponent,
    DriverInfoDialogComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    MaterialComponentsModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class AdminModule { }
