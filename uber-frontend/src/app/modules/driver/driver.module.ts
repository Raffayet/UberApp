import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { DriverRoutingModule } from './driver-routing.module';
import { DriverNotificationComponent } from './components/driver-notification/driver-notification.component';
import { SharedModule } from '../shared/shared.module';
import { DriverDashboardComponent } from './pages/driver-dashboard/driver-dashboard.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RideToTakeDialogComponent } from './components/ride-to-take-dialog/ride-to-take-dialog.component';

@NgModule({
  declarations: [
    DriverNotificationComponent,
    DriverDashboardComponent,
    RideToTakeDialogComponent
  ],
  imports: [
    CommonModule,
    DriverRoutingModule,
    MaterialComponentsModule,
    SharedModule,
    MatDialogModule
  ]
})
export class DriverModule { }
