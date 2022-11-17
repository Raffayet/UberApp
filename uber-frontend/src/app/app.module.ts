import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MdbCollapseModule } from 'mdb-angular-ui-kit/collapse';
import { LoginComponent } from './components/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RadioButtonModule } from 'primeng/radioButton';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {PasswordModule} from 'primeng/password';
import { InputTextModule } from "primeng/inputtext";
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MdbCollapseModule,
    ReactiveFormsModule,
    PasswordModule,
    InputTextModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
