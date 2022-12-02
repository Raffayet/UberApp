import { Component, Input, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent{
    
    @Input()
    navbarLabels: string[];

    @Input()
    navbarPaths: string[];

    @Output()
    chosenOption = new EventEmitter<string>();

    changeOption(path: string){
      this.chosenOption.emit(path);
    }
}
