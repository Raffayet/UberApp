import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminRoutingModule } from './admin-routing.module';
import { RegisterDriverComponent } from './pages/register-driver/register-driver.component';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    RegisterDriverComponent
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
