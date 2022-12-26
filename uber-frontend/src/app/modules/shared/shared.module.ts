import { PaypalPaymentComponent } from 'src/app/modules/shared/components/paypal-payment/paypal-payment.component';
import { LivechatComponent } from './components/livechat/livechat.component';
import { MapComponent } from 'src/app/modules/shared/components/map/map.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryComponent } from './components/history/history.component';
import { SharedRoutingModule } from './shared-routing.module';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { NavbarComponent } from './components/navbar/navbar.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    HistoryComponent,
    LivechatComponent,
    NavbarComponent,
    MapComponent,
    PaypalPaymentComponent
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    MaterialComponentsModule,
    FormsModule,
  ],
  exports: [
    MapComponent,
    NavbarComponent,
    LivechatComponent,
    PaypalPaymentComponent
  ]
})
export class SharedModule { }

