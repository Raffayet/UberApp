import { ReportPageComponent } from './../shared/pages/report-page/report-page.component';
import { RegisteredDriverComponent } from './../auth/pages/registered-driver/registered-driver.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterDriverComponent } from './pages/register-driver/register-driver.component';
import { HistoryComponent } from '../shared/components/history/history.component';

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
    path: 'report',
    component: ReportPageComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
  {
    path: 'history',
    component: HistoryComponent,
    outlet: 'AdminRouter',
    canActivate: [],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
