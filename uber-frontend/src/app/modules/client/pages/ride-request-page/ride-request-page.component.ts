import { RideRequestStateService } from './../../services/ride-request-state.service';
import { trigger, transition, style, animate } from "@angular/animations";

import { Component, OnDestroy, OnInit, ViewChild } from "@angular/core";

import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Observable, of } from "rxjs";
import { environment } from "src/app/environments/environment";
import { Point } from "src/app/model/Point";
import { User } from "src/app/model/User";
import { MapComponent } from "src/app/modules/shared/components/map/map.component";
import { TokenUtilsService } from "src/app/modules/shared/services/token-utils.service";
import { ClientService } from "../../services/client.service";
import { MapSearchResult, MapService } from "../../services/map.service";
import { PaypalService } from 'src/app/modules/shared/services/paypal.service';
import * as SockJS from 'sockjs-client';
import { over, Client, Message as StompMessage} from 'stompjs';
import { NumberValueAccessor } from '@angular/forms';

@Component({
  selector: 'ride-request-page',
  templateUrl: './ride-request-page.component.html',
  styleUrls: ['./ride-request-page.component.css'],
  animations: [
    trigger('firstWindowAnimation', [
      transition(':enter', [
        style({transform: 'translateX(-100%)'}),
        animate('300ms ease-in', style({transform: 'translateX(0%)'}))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({transform: 'translateX(-100%)'}))
      ])
    ]),
    trigger('secondWindowAnimation', [
      transition(':enter', [
        style({transform: 'translateX(100%)'}),
        animate('300ms ease-in', style({transform: 'translateX(0%)'}))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({transform: 'translateX(100%)'}))
      ])
    ])
  ]
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
  }  

  ngOnDestroy(): void {
    this.stateManagement.reset();
    console.log('gadsgsa')
  }

  changePage(eventData: number): void{      
    this.currentPage = eventData;
  }  

  triggerBack()
  {
    this.currentPage = this.currentPage - 1;
  }
}