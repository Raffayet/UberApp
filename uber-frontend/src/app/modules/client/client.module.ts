import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { ClientRoutingModule } from './client-routing.module';
import { RideInvitesPageComponent } from './pages/ride-invites-page/ride-invites-page.component';


@NgModule({
  declarations: [
    RideInvitesPageComponent
  ],
  imports: [
    CommonModule,
    ClientRoutingModule,
    MaterialComponentsModule
  ]
})
export class ClientModule { }
