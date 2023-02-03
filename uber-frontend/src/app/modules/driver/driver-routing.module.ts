import { ReportPageComponent } from './../shared/pages/report-page/report-page.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HistoryComponent } from '../shared/components/history/history.component';
import { UserProfilePageComponent } from '../shared/pages/user-profile-page/user-profile-page.component';
import { RidesToDoComponent } from './pages/rides-to-do/rides-to-do.component';
import { DriverGuard } from './guard/driver.guard';

const routes: Routes = [  
  {
    path: 'profile-page',
    component: UserProfilePageComponent,
    outlet: 'DriverRouter',
    canActivate: [DriverGuard],
  },
  {
    path: 'rides-to-do',
    component: RidesToDoComponent,
    outlet: 'DriverRouter',
    canActivate: [DriverGuard],
  },
  {
    path: 'report',
    component: ReportPageComponent,
    outlet: 'DriverRouter',
    canActivate: [DriverGuard],
  },
  {
    path: 'history',
    component: HistoryComponent,
    outlet: 'DriverRouter',
    canActivate: [DriverGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
