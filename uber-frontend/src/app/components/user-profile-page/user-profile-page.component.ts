import { Component } from '@angular/core';
import { User } from 'src/app/model/User';
import { TokenUtilsService } from 'src/app/services/token-utils.service';
import { Validators, FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-user-profile-page',
  templateUrl: './user-profile-page.component.html',
  styleUrls: ['./user-profile-page.component.css']
})
export class UserProfilePageComponent {

  loggedUser: User | null;
  infoForm: FormGroup;

  constructor(private tokenUtilsService: TokenUtilsService){}

  ngOnInit() {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();

      this.infoForm = new FormGroup({
        'email': new FormControl(this.loggedUser?.email, Validators.required),
        'name': new FormControl(this.loggedUser?.name, Validators.required),
        'surname': new FormControl(this.loggedUser?.surname, Validators.required),
        'city': new FormControl(this.loggedUser?.city, Validators.required),
        'phone': new FormControl(this.loggedUser?.phoneNumber, Validators.required),
    });    
  }


  onSave(){

  }
}
