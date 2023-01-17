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
import {MatSortModule} from '@angular/material/sort';
import { RouteDetailDialogComponent } from './components/route-detail-dialog/route-detail-dialog.component';


@NgModule({
  declarations: [
    HistoryComponent,
    LivechatComponent,
    NavbarComponent,
    MapComponent,
    PaypalPaymentComponent,
    RouteDetailDialogComponent,
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    MaterialComponentsModule,
    FormsModule,
    MatSortModule
  ],
  exports: [
    MapComponent,
    NavbarComponent,
    LivechatComponent,
    PaypalPaymentComponent
  ]
})
export class SharedModule { }

