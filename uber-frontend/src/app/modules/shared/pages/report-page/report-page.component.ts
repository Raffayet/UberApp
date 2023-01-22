import { DateRange } from './../../../../model/DateRange';
import { ReportService } from './../../services/report.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ReportResponse } from 'src/app/model/ReportResponse';
import { TokenUtilsService } from '../../services/token-utils.service';

@Component({
  selector: 'app-report-page',
  templateUrl: './report-page.component.html',
  styleUrls: ['./report-page.component.css']
})
export class ReportPageComponent implements OnInit{
  basicData: any;
  basicOptions: any;
  range = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null),
  });
  minDate: Date;
  maxDate: Date;
  protected userType: string;

  constructor(private reportService:ReportService, private tokenUtilsService: TokenUtilsService){
    let tomorow = new Date();
    tomorow.setDate(new Date().getDate()+1);
    let lastYear = new Date();
    lastYear.setFullYear(new Date().getFullYear()-1);
    console.log(tomorow);
    console.log(lastYear);
    this.maxDate = tomorow;
    this.minDate = lastYear;
    this.userType = this.tokenUtilsService.getUserFromToken()?.role as string;
  }

  

  ngOnInit(): void {
    let tomorow = new Date();
    tomorow.setDate(new Date().getDate()+1);
    let lastWeek = new Date();
    lastWeek.setDate(new Date().getDate()-6);
    this.range.get('start')?.setValue(lastWeek);
    this.range.get('end')?.setValue(tomorow);
    this.generateReport();
  }

  generateReport():void{
      console.log(this.range.value);
      const dateRange:DateRange = this.range?.value;
      if(dateRange.start && dateRange.end){
        this.reportService.generateReportForDateRange(dateRange)
          .subscribe({
            next: (response: ReportResponse) => {
              console.log("USPESNO");
              console.log(response);

              this.basicData = {
                labels:response.ridesPerDay.labels,
                datasets:[
                  {
                    label: 'Rides Per Day',
                    backgroundColor:'#42A5F5',
                    data: response.ridesPerDay.values
                  },{
                    label: 'Km Per Day',
                    backgroundColor:'#19bf45',
                    data: response.kmPerDay.values
                  },{
                    label: 'Money'+this.userType==="CLIENT"?'Spend':'Made'+' Per Day',
                    backgroundColor:'#3916c4',
                    data: response.moneyPerDay.values
                  }
                ]
              }
            },
            error: (err: HttpErrorResponse) => {          
              console.log("NEUSPESNO");
            }
          });
      }
  }

}
