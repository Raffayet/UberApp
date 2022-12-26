import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { ClientRoutingModule } from './client-routing.module';
import { RideInvitesPageComponent } from './pages/ride-invites-page/ride-invites-page.component';
import { LocationPickerComponent } from './components/location-picker/location-picker.component';
import { RideRequestPageComponent } from './pages/ride-request-page/ride-request-page.component';
import { SharedModule } from '../shared/shared.module';
import { FormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { RideInvitationComponent } from './components/ride-invitation/ride-invitation.component';
import { RideTypeComponent } from './components/ride-type/ride-type.component';

@NgModule({
  declarations: [
    RideInvitesPageComponent,
    RideRequestPageComponent,
    LocationPickerComponent,
    RideInvitationComponent,
    RideTypeComponent,
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    MaterialComponentsModule,
    SharedModule,
    FormsModule,
    DragDropModule,
  ]
})
export class ClientModule { }
