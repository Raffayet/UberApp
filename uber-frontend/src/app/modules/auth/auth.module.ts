import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { AuthRoutingModule } from './auth-routing.module';
import { RegisteredDriverComponent } from './pages/registered-driver/registered-driver.component';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    AuthRoutingModule,
    MaterialComponentsModule
  ]
})
export class AuthModule { }
