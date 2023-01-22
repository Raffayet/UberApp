import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-ride-review',
  templateUrl: './ride-review.component.html',
  styleUrls: ['./ride-review.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class RideReviewComponent implements OnInit{
	reviewFormGroup:FormGroup;


	ngOnInit(): void {
		this.reviewFormGroup = new FormGroup({
		  rating: new FormControl(1,[Validators.required]),
		  comment: new FormControl('', [Validators.required]),
		});
		
	  }
  
}
