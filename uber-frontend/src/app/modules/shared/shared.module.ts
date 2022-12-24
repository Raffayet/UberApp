import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HistoryComponent } from './components/history/history.component';
import { SharedRoutingModule } from './shared-routing.module';
import { MaterialComponentsModule } from '../material-components/material-components.module';


@NgModule({
  declarations: [
    HistoryComponent
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    MaterialComponentsModule
  ]
})
export class SharedModule { }

