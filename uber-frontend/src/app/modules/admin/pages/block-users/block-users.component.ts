import { UserService } from 'src/app/modules/shared/services/user.service';
import { Component, OnInit } from '@angular/core';
import { map, Observable, startWith } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-block-users',
  templateUrl: './block-users.component.html',
  styleUrls: ['./block-users.component.css']
})
export class BlockUsersComponent implements OnInit{
  
  options: string[] = ['One', 'Two', 'Three'];
  filteredOptions: Observable<string[]>;
  blockForm:FormGroup;
  
  constructor(private userService:UserService){
  }

  ngOnInit(): void {
    this.blockForm = new FormGroup({
      emailControl: new FormControl('',[Validators.required]),
      description: new FormControl(''),
    });
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers().subscribe({
      next: (data:string[]) => {
        console.log(data);
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
    
  }
}
