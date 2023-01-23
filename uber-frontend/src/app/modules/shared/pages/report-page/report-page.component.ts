import { UserService } from 'src/app/modules/shared/services/user.service';
import { DateRange } from './../../../../model/DateRange';
import { ReportService } from './../../services/report.service';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ReportResponse } from 'src/app/model/ReportResponse';
import { TokenUtilsService } from '../../services/token-utils.service';
import { map, Observable, startWith } from 'rxjs';

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
  emailControl = new FormControl('');
  minDate: Date;
  maxDate: Date;
  protected userType: string;
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;

  constructor(private reportService:ReportService, private tokenUtilsService: TokenUtilsService, private userService:UserService){
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
    let today = new Date();
    let lastWeek = new Date();
    today.setHours(12);
    lastWeek.setDate(new Date().getDate()-7);
    lastWeek.setHours(12);
    this.range.get('start')?.setValue(lastWeek);
    this.range.get('end')?.setValue(today);
    this.generateReport();
    
    this.getUsers();
  }
  
  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }
 
  getUsers() {
    this.userService.getUsers().subscribe({
      next: (data:string[]) => {
        console.log(data);
        this.options = ['All Users'];
        this.options.push(...data);
        this.filteredOptions = this.emailControl.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '')),
        );
      },
      error: (err) => {
        console.log(err.error.message);
      },
    });
  }
  
  generateReport():void{
      console.log(this.range.value);
      console.log(this.emailControl.value);
      const dateRange:DateRange = this.range?.value;
      const selectedUser:string = this.emailControl.value as string;
      if(dateRange.start && dateRange.end){
        this.reportService.generateReportForDateRange(dateRange, this.userType==='ADMIN'?selectedUser:null)
          .subscribe({
            next: (response: ReportResponse) => {
              
              const report = this.userType === "ADMIN"&&(!selectedUser || selectedUser==="All Users")?response.adminReport:response;

              this.basicData = {
                labels:report.ridesPerDay.labels,
                datasets:[
                  {
                    label: 'Rides Per Day',
                    backgroundColor:'#42A5F5',
                    data: report.ridesPerDay.values
                  },{
                    label: 'Km Per Day',
                    backgroundColor:'#19bf45',
                    data: report.kmPerDay.values
                  },{
                    label: 'Money '+(this.userType==="CLIENT"?'Spend':'Made'+' Per Day'),
                    backgroundColor:'#3916c4',
                    data: report.moneyPerDay.values
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
