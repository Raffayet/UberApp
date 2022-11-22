import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { catchError, lastValueFrom, throwError } from 'rxjs';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  submitted = false;
  success = false;
  res = '';

  constructor(private loginService: LoginService) {}
  
  ngOnInit() {
      this.loginForm = new FormGroup({
          'email': new FormControl('', Validators.required),
          'password': new FormControl('', Validators.required)
      });
  }
  
  get formFields() { return this.loginForm.controls; }
  
  onSubmit() { 
      this.submitted = true;

        this.loginService.logIn(this.loginForm, this.success)
        .pipe(catchError(err => {return throwError(() => {new Error('greska')} )}))
        .subscribe({
          next: (res) => {
            console.log('uspesno');
            this.success = true;
          },
          error: (err) => {
            this.success = false;
          },
        });

      // let data = await lastValueFrom(response);
      // console.log(data)
  }

}
