import { ReportPageComponent } from './../shared/pages/report-page/report-page.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RideRequestPageComponent } from 'src/app/modules/client/pages/ride-request-page/ride-request-page.component';
import { HistoryComponent } from '../shared/components/history/history.component';
import { UserProfilePageComponent } from '../shared/pages/user-profile-page/user-profile-page.component';
import { ClientDashboardComponent } from './pages/client-dashboard/client-dashboard.component';
import { RideInvitesPageComponent } from './pages/ride-invites-page/ride-invites-page.component';

const routes: Routes = [  
  {
    path: 'request-ride-page',
    component: RideRequestPageComponent,
    outlet: 'ClientRouter',
    canActivate: [],
  },
  {
    path: 'profile-page',
    component: UserProfilePageComponent,
    outlet: 'ClientRouter',
    canActivate: [],
  },
  {
    path: 'ride-invites',
    component: RideInvitesPageComponent,
    outlet: 'ClientRouter',
    canActivate: [],
  },
  {
    path: 'history',
    component: HistoryComponent,
    outlet: 'ClientRouter',
    canActivate: [],
  },
  {
    path: 'report',
    component: ReportPageComponent,
    outlet: 'ClientRouter',
    canActivate: [],
  },
  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
