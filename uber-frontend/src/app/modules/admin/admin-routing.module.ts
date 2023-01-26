import { DriverInfoRequestComponent } from './pages/driver-info-request/driver-info-request.component';
import { UserProfilePageComponent } from './../shared/pages/user-profile-page/user-profile-page.component';
import { LivechatComponent } from './../shared/components/livechat/livechat.component';

import { BlockUsersComponent } from './pages/block-users/block-users.component';
import { ReportPageComponent } from './../shared/pages/report-page/report-page.component';
import { RegisteredDriverComponent } from './../auth/pages/registered-driver/registered-driver.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterDriverComponent } from './pages/register-driver/register-driver.component';
import { HistoryComponent } from '../shared/components/history/history.component';
import { HomepageComponent } from './pages/homepage/homepage.component';

const routes: Routes = [
  {
    path: 'register-driver',
    component: RegisterDriverComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'register-confirmation',
    component: RegisteredDriverComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'driver-info-request',
    component: DriverInfoRequestComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'report',
    component: ReportPageComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },{
    path: 'livechat',
    component: LivechatComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'block-users',
    component: BlockUsersComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'history',
    component: HistoryComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'homepage',
    component: HomepageComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'profile',
    component: UserProfilePageComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
