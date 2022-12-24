import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { User } from 'src/app/model/User';
import { PaypalService } from 'src/app/modules/client/services/paypal.service';

@Component({
  selector: 'app-paypal-payment',
  templateUrl: './paypal-payment.component.html',
  styleUrls: ['./paypal-payment.component.css']
})
export class PaypalPaymentComponent implements OnInit{

  @Input() loggedUser : User | null; 
  currentAmount : number;
  tokens: number | string;
  
  constructor(private paypalService: PaypalService){}

  @ViewChild('paypalRef', { static: true }) private paypalRef: ElementRef;
  
  ngOnInit(): void {
    this.getAmountOfTokens();

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
            console.log(details);
            
          })
      },

      onError: (error: any) => {console.log(error);}
      

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
