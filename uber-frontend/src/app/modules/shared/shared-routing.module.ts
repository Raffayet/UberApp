import { SharedGuard } from './guard/shared.guard';
import { ReportPageComponent } from './pages/report-page/report-page.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HistoryComponent } from './components/history/history.component';
import { UserProfilePageComponent } from './pages/user-profile-page/user-profile-page.component';

export const routes: Routes = [
  {
    path: "history",
    pathMatch: "full",
    component: HistoryComponent,
    canActivate: [SharedGuard],
  },
  { path: 'user-profile',
    pathMatch: "full",
    component: UserProfilePageComponent,
    canActivate: [SharedGuard],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SharedRoutingModule { }
