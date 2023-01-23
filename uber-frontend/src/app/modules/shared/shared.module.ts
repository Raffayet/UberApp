import { PaypalPaymentComponent } from 'src/app/modules/shared/components/paypal-payment/paypal-payment.component';
import { LivechatComponent } from './components/livechat/livechat.component';
import { MapComponent } from 'src/app/modules/shared/components/map/map.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryComponent } from './components/history/history.component';
import { SharedRoutingModule } from './shared-routing.module';
import { MaterialComponentsModule } from '../material-components/material-components.module';
import { NavbarComponent } from './components/navbar/navbar.component';
<<<<<<< HEAD
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ReportPageComponent } from './pages/report-page/report-page.component';
=======
import { FormsModule } from '@angular/forms';
import {MatSortModule} from '@angular/material/sort';
import { RouteDetailDialogComponent } from './components/route-detail-dialog/route-detail-dialog.component';
import { OrderExistingRideDialogComponent } from './components/order-existing-ride-dialog/order-existing-ride-dialog.component';
import { CalendarModule } from 'primeng/calendar';
import { ClientsInfoDialogComponent } from './components/clients-info-dialog/clients-info-dialog.component';
>>>>>>> develop


@NgModule({
  declarations: [
    HistoryComponent,
    LivechatComponent,
    NavbarComponent,
    MapComponent,
    PaypalPaymentComponent,
<<<<<<< HEAD
    ReportPageComponent,
=======
    RouteDetailDialogComponent,
    OrderExistingRideDialogComponent,
    ClientsInfoDialogComponent
>>>>>>> develop
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    MaterialComponentsModule,
    FormsModule,
<<<<<<< HEAD
    ReactiveFormsModule
=======
    MatSortModule,
    CalendarModule
>>>>>>> develop
  ],
  exports: [
    MapComponent,
    NavbarComponent,
    LivechatComponent,
    PaypalPaymentComponent
  ]
})
export class SharedModule { }

