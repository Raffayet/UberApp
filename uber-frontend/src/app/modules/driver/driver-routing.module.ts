import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HistoryComponent } from '../shared/components/history/history.component';
import { UserProfilePageComponent } from '../shared/pages/user-profile-page/user-profile-page.component';
import { RidesToDoComponent } from './pages/rides-to-do/rides-to-do.component';

const routes: Routes = [  
  {
    path: 'profile-page',
    component: UserProfilePageComponent,
    outlet: 'DriverRouter',
    canActivate: [],
  },
  {
    path: 'rides-to-do',
    component: RidesToDoComponent,
    outlet: 'DriverRouter',
    canActivate: [],
  },
  {
    path: 'history',
    component: HistoryComponent,
    outlet: 'DriverRouter',
    canActivate: [],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
