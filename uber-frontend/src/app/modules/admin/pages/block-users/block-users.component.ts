import { BlockUserRequest } from './../../../../model/BlockUserRequest';
import { UserService } from 'src/app/modules/shared/services/user.service';
import { Component, OnInit } from '@angular/core';
import { map, Observable, startWith } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/model/User';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-block-users',
  templateUrl: './block-users.component.html',
  styleUrls: ['./block-users.component.css']
})
export class BlockUsersComponent implements OnInit{
  
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;
  blockForm:FormGroup;
  
  constructor(private userService:UserService, private toaster:ToastrService){
  }

  ngOnInit(): void {
    this.blockForm = new FormGroup({
      emailControl: new FormControl('',[Validators.required]),
      description: new FormControl(''),
    });
    this.getNotBlockedUsers();
  }

  getNotBlockedUsers() {
    this.userService.getNotBlockedUsers().subscribe({
      next: (data:string[]) => {
        this.options = data;
        this.filteredOptions = this.blockForm.get('emailControl')!.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '')),
        );
      },
      error: (err) => {
        console.log(err.error.message);
      },
    });
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    
    return this.options.filter(option => option.toLowerCase().includes(filterValue));
  }

  public banUser(){
    if(this.blockForm.valid){
      const formValues = this.blockForm.value;
      console.log(formValues);
      const data:BlockUserRequest = {
        userEmail:formValues.emailControl,
        description:formValues.description
      }
      this.userService.blockUser(data).subscribe({
        next: (responseData:User) => {
          this.toaster.success(`User ${responseData.name+' '+responseData.surname} with email:${responseData.email} has been successfully blocked.`,"User Blocked",{timeOut:5000});
          this.blockForm.get('emailControl')?.setValue("");
          this.blockForm.get('description')?.setValue("");
          this.options = this.options.filter(option=>option !== responseData.email);
          this.filteredOptions = this.blockForm.get('emailControl')!.valueChanges.pipe(
            startWith(''),
            map(value => this._filter(value || '')),
          );
        },
        error: (err) => {
          console.log(err.error.message);
        },
      });
    }
  }
}
