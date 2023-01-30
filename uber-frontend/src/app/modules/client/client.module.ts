import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { ClientRoutingModule } from './client-routing.module';
import { LocationPickerComponent } from './components/location-picker/location-picker.component';
import { RideRequestPageComponent } from './pages/ride-request-page/ride-request-page.component';
import { SharedModule } from '../shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { RideInvitationComponent } from './components/ride-invitation/ride-invitation.component';
import { RideTypeComponent } from './components/ride-type/ride-type.component';
import { MatDialogModule } from '@angular/material/dialog';
import { RideInviteDialogComponent } from './components/ride-invite-dialog/ride-invite-dialog.component';
import { CalendarModule } from 'primeng/calendar';
import { TimerDialogComponent } from './components/timer-dialog/timer-dialog.component';
import { RideReviewComponent } from './components/ride-review/ride-review.component';

@NgModule({
  declarations: [
    RideRequestPageComponent,
    LocationPickerComponent,
    RideInvitationComponent,
    RideTypeComponent,
    RideInviteDialogComponent,
    TimerDialogComponent,
    RideReviewComponent,
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    MaterialComponentsModule,
    SharedModule,
    FormsModule,
    DragDropModule,
    MatDialogModule,
    CalendarModule,
    ReactiveFormsModule
  ],
  exports:[
    RideRequestPageComponent
  ]
})
export class ClientModule { }
