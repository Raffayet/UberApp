import { Component } from '@angular/core';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/services/token-utils.service';
import { Validators, FormControl, FormGroup } from '@angular/forms';
import { ViewportScroller } from '@angular/common';
import { UserService } from 'src/app/services/user.service';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-profile-page',
  templateUrl: './user-profile-page.component.html',
  styleUrls: ['./user-profile-page.component.css']
})
export class UserProfilePageComponent {

  loggedUser: User | null;
  infoForm: FormGroup;
  passwordForm: FormGroup;

  constructor(private tokenUtilsService: TokenUtilsService, private viewportScroller: ViewportScroller,
              private userService: UserService, private toastr: ToastrService){}

  ngOnInit() {    
      this.loggedUser = this.tokenUtilsService.getUserFromToken();
      
      this.infoForm = new FormGroup({
        'email': new FormControl(this.loggedUser?.email, Validators.required),
        'name': new FormControl(this.loggedUser?.name, Validators.required),
        'surname': new FormControl(this.loggedUser?.surname, Validators.required),
        'city': new FormControl(this.loggedUser?.city, Validators.required),
        'phone': new FormControl(this.loggedUser?.phoneNumber, Validators.required),
    });    

    this.passwordForm = new FormGroup({
      'oldPassword': new FormControl('', [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}")]),
      'newPassword': new FormControl('', [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}")]),
      'confirmNewPassword': new FormControl('', [Validators.required, Validators.pattern("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}")]),
    });    
  }

  get oldPassword(){
    return this.passwordForm.get("oldPassword");
  }

  get newPassword(){
    return this.passwordForm.get("newPassword");
  }

  get confirmNewPassword(){
    return this.passwordForm.get("confirmNewPassword");
  }

  get firstName(){
    return this.infoForm.get("name");
  }
  get lastName(){
    return this.infoForm.get("surname");
  }
  get email(){
    return this.infoForm.get("email");
  }
  get city(){
    return this.infoForm.get("city");
  }
  get telephone(){
    return this.infoForm.get("phone");
  }


  onClick(elementId: string): void { 
    this.viewportScroller.scrollToAnchor(elementId);
  }

  onSave(){
      this.userService.updatePersonalInfo(this.infoForm)
      .subscribe({
        next: (token: string) => {
          localStorage.setItem("user", token);
          this.toastr.success("You have successfully updated personal info!")
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.warning(err.error);
        }
      });
  }

  onPasswordChange(){
      this.userService.updatePassword(this.passwordForm, this.loggedUser?.email as string)
      .subscribe({
        next: (response: string) => {
          this.toastr.success(response);
          this.passwordForm.reset();
        },
        error: (err: HttpErrorResponse) => {
          this.toastr.warning(err.error);
        }
      });
  }
}
