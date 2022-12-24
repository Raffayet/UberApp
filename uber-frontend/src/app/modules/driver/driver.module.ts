import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { DriverRoutingModule } from './driver-routing.module';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    DriverRoutingModule,
    MaterialComponentsModule
  ]
})
export class DriverModule { }
