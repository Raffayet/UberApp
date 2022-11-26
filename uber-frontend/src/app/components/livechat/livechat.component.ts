import "../init.js";
import { Component } from '@angular/core';
import { LivechatService } from 'src/app/services/livechat.service';
import { TokenUtilsService } from 'src/app/services/token-utils.service';
import * as SockJS from 'sockjs-client';
import { over, Client } from 'stompjs';
import { LoginComponent } from "../login/login.component.js";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs";

export interface Message{
  senderEmail: string,
  senderFirstName: string | null,
  senderLastName: string | null,
  receiverEmail: string,
  receiverFirstName: string | null,
  receiverLastName: string | null,
  content: string,
  date: string,
  status: string
}

@Component({
  selector: 'app-livechat',
  templateUrl: './livechat.component.html',
  styleUrls: ['./livechat.component.css'],
})

export class LivechatComponent {

  private stompClient : Client;
  private loggedUsername : string | null;
  loggedRole: string | null;
  
  allUsersFromMessages: string[] = ["Jovan", "Petar"];
  userChat : Message[] = [];
  adminChat : Map<string, Message[]>;
  message: string;

  constructor(private livechatService: LivechatService, private tokenUtilsService: TokenUtilsService) {}
  
  ngOnInit() {
    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);
  }

  onConnected = () => {
      this.loggedUsername = this.tokenUtilsService.getUsernameFromToken();     
      this.loggedRole = this.tokenUtilsService.getRoleFromToken();
      
      if(this.loggedRole === 'ADMIN'){
        //get request for admin
        this.stompClient.subscribe("/chatroom/public", this.onPublicMessageReceived);

        // let msgs:Observable<Message[]> = this.livechatService.findAllMessagesForUser(this.loggedUsername as string);
        // msgs.subscribe(val => this.userChat = val);   

      }else{       
        this.stompClient.subscribe("/user/" + this.loggedUsername  + "/private", this.onPrivateMessageReceived);  

        let msgs:Observable<Message[]> = this.livechatService.findAllMessagesForUser(this.loggedUsername as string);
        msgs.subscribe(val => this.userChat = val);        
      }    
          
  }

  onPublicMessageReceived = (payload: any) => {
      let payloadData = JSON.parse(payload.body);
      console.log(payloadData);
  }

  onPrivateMessageReceived = (payload: any) => {
      let payloadData = JSON.parse(payload.body);
      // if(this.userChats.get(payloadData.senderName)){
      //     this.userChats.get(payloadData.senderName)?.push(payloadData);
      // }
      // else{
      //     let messages : string[] = [];
      //     messages.push(payloadData);

      //     this.userChats.set(payloadData.senderName, messages);
      // }
      console.log(payloadData);      
  }

  onError = () => {
    console.log("Error");    
  }

  handleMessage = () => {
    let message = "Pozdrav!";
  }

  sendPublicMessage = () => {
    if(this.stompClient){
        let message = "Pozdrav!";

        let chatMessage : Message = {
          senderEmail: this.loggedUsername as string,
          senderFirstName: null,
          senderLastName: null,
          receiverEmail: "support",
          receiverFirstName: null,
          receiverLastName: null,
          content : message,
          date: new Date().toDateString(),
          status: 'UNREAD'
        }
    
        this.stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
    }
  
  }

  sendPrivateMessage = () => {
    if(this.stompClient){
      let message = "Pozdrav!";

      let chatMessage: Message = {
        senderEmail: this.loggedUsername as string,
        senderFirstName: null,
        senderLastName: null,
        receiverEmail: "support",
        receiverFirstName: null,
        receiverLastName: null,
        content: message,
        date: new Date().toDateString(),
        status: 'UNREAD'
      }
  
      this.stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
    }
  }

}
