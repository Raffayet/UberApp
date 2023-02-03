import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { User } from 'src/app/model/User';
import { PaypalService } from '../../services/paypal.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-paypal-payment',
  templateUrl: './paypal-payment.component.html',
  styleUrls: ['./paypal-payment.component.css']
})
export class PaypalPaymentComponent implements OnInit{

  @Input() loggedUser : User | null; 
  currentAmount : number;
  tokens: number | string;
  
  constructor(private paypalService: PaypalService, private toastService: ToastrService){}

  @ViewChild('paypalRef', { static: true }) private paypalRef: ElementRef;
  
  ngOnInit(): void {
    this.getAmountOfTokens();

    let that = this;
    window.paypal.Buttons({
      style: {
        color: 'blue',
        shape: 'pill'        
      },      
      createOrder: (data:any, actions:any) => {
          return actions.order.create({
            purchase_units: [
              {
                amount: {
                  value: this.tokens,
                  currency_code: 'USD'
                }
              }
            ]
          })
      },

      onApprove: (data: any, actions: any) => {
          return actions.order.capture().then(function(details: any){
            that.paypalService.addAmountOfTokens(that.loggedUser?.email as string, that.tokens as number).subscribe({
              next: (data) => {
                  that.currentAmount = data as number;
                  that.toastService.success("Successfully added tokens!");
              },
              error: (err) => {
                that.toastService.warning("Something went wrong with payment!");
              },
            });
          })
      },

      onError: (error: any) => {that.toastService.warning("Something went wrong with payment!");}
      

    }).render(this.paypalRef.nativeElement);
  }

  valueChange(event: Event){
    if(this.tokens === 0){
      this.tokens = '0';
    }
  }

  getAmountOfTokens(){
    this.paypalService.getAmountOfTokens(this.loggedUser?.email as string).subscribe(
      (data: number) => this.currentAmount = data
    );
  }

}
