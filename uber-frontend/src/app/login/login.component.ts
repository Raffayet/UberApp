import { Component, OnInit } from '@angular/core';
import { Validators,FormControl,FormGroup } from '@angular/forms';
import { FormsModule } from "@angular/forms";
import {PasswordModule} from 'primeng/password';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  submitted = false;

  constructor() { }
  
  ngOnInit() {
      this.loginForm = new FormGroup({
          'username': new FormControl('', Validators.required),
          'password': new FormControl('', Validators.required)
      });
  }
  
  get formFields() { return this.loginForm.controls; }
  
  onSubmit() { 
      this.submitted = true;
      alert(JSON.stringify(this.loginForm.value));
  }

}
