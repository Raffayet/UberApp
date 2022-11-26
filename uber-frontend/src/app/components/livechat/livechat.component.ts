import "../init.js";
import { Component } from '@angular/core';
import { LivechatService } from 'src/app/services/livechat.service';
import { TokenUtilsService } from 'src/app/services/token-utils.service';
import * as SockJS from 'sockjs-client';
import { over, Client, Message as StompMessage} from 'stompjs';
import { LoginComponent } from "../login/login.component.js";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs";
import { Message, User } from "src/app/helpers/common-interfaces.js";

@Component({
  selector: 'app-livechat',
  templateUrl: './livechat.component.html',
  styleUrls: ['./livechat.component.css'],
})

export class LivechatComponent {

  private stompClient : Client;
  loggedUser : User | null;
  allUsersFromMessages: User[] = [];
  userChat : Message[] = [];
  adminChat : Map<string, Message[]>;
  messageToSend: string = "";

  constructor(private livechatService: LivechatService, private tokenUtilsService: TokenUtilsService) {}
  
  ngOnInit() {
    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);
  }

  onConnected = () => {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();
      
      if(this.loggedUser?.role === 'ADMIN'){
        //get request for admin
        this.stompClient.subscribe("/chatroom/public", this.onPublicMessageReceived);

        let users:Observable<User[]> = this.livechatService.getAllUsersFromMessages();
        users.subscribe(val => this.allUsersFromMessages = val);
        
        this.livechatService.getAdminChat().subscribe(val => {this.adminChat = val; console.log(val);
        });

      }else{       
        this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/private", this.onPrivateMessageReceived);  

        let msgs:Observable<Message[]> = this.livechatService.findAllMessagesForUser(this.loggedUser?.email as string);
        msgs.subscribe(val => this.userChat = val);        
      }    
          
  }

  onPublicMessageReceived = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      // console.log(payloadData);
  }

  onPrivateMessageReceived = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      // if(this.userChats.get(payloadData.senderName)){
      //     this.userChats.get(payloadData.senderName)?.push(payloadData);
      // }
      // else{
      //     let messages : string[] = [];
      //     messages.push(payloadData);

      //     this.userChats.set(payloadData.senderName, messages);
      // }
      this.userChat.push(payloadData);
  }

  onError = () => {
    console.log("Error");    
  }

  handleMessage = () => {
    let message = "Pozdrav!";
  }

  sendPublicMessage = () => {
    if(this.stompClient){

        let chatMessage : Message = {
          senderEmail: this.loggedUser?.email as string,
          senderFirstName: this.loggedUser?.name as string,
          senderLastName: this.loggedUser?.surname as string,
          receiverEmail: "support",
          receiverFirstName: null,
          receiverLastName: null,
          content : this.messageToSend,
          date: new Date(),
          status: 1
        }        
        
        this.messageToSend = "";
        this.userChat.push(chatMessage);
        this.stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
    }
  
  }

  sendPrivateMessage = () => {
    if(this.stompClient){

      let chatMessage: Message = {
        senderEmail: this.loggedUser?.email as string,
        senderFirstName: null,
        senderLastName: null,
        receiverEmail: "sasa@gmail.com",
        receiverFirstName: null,
        receiverLastName: null,
        content: this.messageToSend,
        date: new Date(),
        status: 1
      }
      
      this.messageToSend = "";
      // this.adminChat.set();
      this.stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
    }
  }

}
