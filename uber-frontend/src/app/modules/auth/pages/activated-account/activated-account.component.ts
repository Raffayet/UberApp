import { Component } from '@angular/core';
import {MatIconModule} from '@angular/material/icon'
import { Router } from '@angular/router';

@Component({
  selector: 'app-activated-account',
  templateUrl: './activated-account.component.html',
  styleUrls: ['./activated-account.component.css']
})
export class ActivatedAccountComponent {

  constructor(private router:Router){}


  goToLogin(){
    this.router.navigateByUrl('/login');
  }
}
