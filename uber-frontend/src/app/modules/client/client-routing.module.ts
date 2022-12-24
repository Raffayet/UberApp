import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RideRequestPageComponent } from 'src/app/modules/client/pages/ride-request-page/ride-request-page.component';
import { UserProfilePageComponent } from '../shared/pages/user-profile-page/user-profile-page.component';
import { ClientDashboardComponent } from './pages/client-dashboard/client-dashboard.component';

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
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientRoutingModule { }
