import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserProfilePageComponent } from '../shared/pages/user-profile-page/user-profile-page.component';

const routes: Routes = [  
  {
    path: 'profile-page',
    component: UserProfilePageComponent,
    outlet: 'DriverRouter',
    canActivate: [],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
