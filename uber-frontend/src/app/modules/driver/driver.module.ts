import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { DriverRoutingModule } from './driver-routing.module';
import { DriverNotificationComponent } from './components/driver-notification/driver-notification.component';
import { SharedModule } from '../shared/shared.module';
import { DriverDashboardComponent } from './pages/driver-dashboard/driver-dashboard.component';

@NgModule({
  declarations: [
    DriverNotificationComponent,
    DriverDashboardComponent
  ],
  imports: [
    CommonModule,
    DriverRoutingModule,
    MaterialComponentsModule,
    SharedModule,
  ]
})
export class DriverModule { }
