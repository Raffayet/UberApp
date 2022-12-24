import { Component } from '@angular/core';
import {MatIconModule} from '@angular/material/icon'
import { Router } from '@angular/router';

@Component({
  selector: 'app-registered-account-page',
  templateUrl: './registered-account-page.component.html',
  styleUrls: ['./registered-account-page.component.css']
})
export class RegisteredAccountPageComponent {

  constructor(private router:Router){}

  goToLogin(){
    this.router.navigateByUrl('/login');
  }
}
