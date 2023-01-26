import { RideRequestStateService } from './../../services/ride-request-state.service';

import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";
import { User } from "src/app/model/User";
import { TokenUtilsService } from "src/app/modules/shared/services/token-utils.service";
import { ClientService } from "../../services/client.service";

@Component({
  selector: 'ride-request-page',
  templateUrl: './ride-request-page.component.html',
  styleUrls: ['./ride-request-page.component.css'],
})

export class RideRequestPageComponent implements OnInit, OnDestroy{

  loggedUser: User | null;

  firstWindow: boolean = true;

  secondWindow: boolean = false;

  currentAmount: number;

  currentPage: number = 1;
  
  constructor(private tokenUtilsService: TokenUtilsService, private clientService: ClientService, protected stateManagement: RideRequestStateService) {}

  ngOnInit() { 
    this.loggedUser = this.tokenUtilsService.getUserFromToken(); 
    console.log(this.loggedUser);  
  }  

  ngOnDestroy(): void {
    this.stateManagement.reset();
  }

  changePage(eventData: number): void{      
    this.currentPage = eventData;
  }

  triggerBack()
  {
    this.currentPage = this.currentPage - 1;
  }
}