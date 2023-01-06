import { Component, Inject, OnInit, EventEmitter, Output } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-rejection-dialog',
  templateUrl: './rejection-dialog.component.html',
  styleUrls: ['./rejection-dialog.component.css']
})
export class RejectionDialogComponent {
  confirmButtonText = "Confirm";
  reasonForRejection = '';
  @Output() reasonForRejectionEmitter = new EventEmitter<string>();

  constructor(
    @Inject(MAT_DIALOG_DATA) private data: any,
    private dialogRef: MatDialogRef<RejectionDialogComponent>, private toastr: ToastrService) {
      if(data){
        if (data.buttonText) {
          this.confirmButtonText = data.buttonText.ok || this.confirmButtonText;
        }
      }
  }

  onConfirmClick(): void {
    if(!this.reasonForRejection || this.reasonForRejection == undefined || this.reasonForRejection == "")
    {
      this.toastr.error("You must send an explanation to your rejection of this drive!");
    }

    else{
      this.reasonForRejectionEmitter.emit(this.reasonForRejection);
    }
  }
}
