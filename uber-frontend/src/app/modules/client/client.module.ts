import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { ClientRoutingModule } from './client-routing.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    ClientRoutingModule,
    MaterialComponentsModule
  ]
})
export class ClientModule { }
