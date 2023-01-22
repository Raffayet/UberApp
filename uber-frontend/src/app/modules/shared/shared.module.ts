import { PaypalPaymentComponent } from 'src/app/modules/shared/components/paypal-payment/paypal-payment.component';
import { LivechatComponent } from './components/livechat/livechat.component';
import { MapComponent } from 'src/app/modules/shared/components/map/map.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryComponent } from './components/history/history.component';
import { SharedRoutingModule } from './shared-routing.module';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReportPageComponent } from './pages/report-page/report-page.component';


@NgModule({
  declarations: [
    HistoryComponent,
    LivechatComponent,
    NavbarComponent,
    MapComponent,
    PaypalPaymentComponent,
    ReportPageComponent,
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    MaterialComponentsModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    MapComponent,
    NavbarComponent,
    LivechatComponent,
    PaypalPaymentComponent
  ]
})
export class SharedModule { }

