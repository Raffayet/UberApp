import "../../../../init.js";
import { Component } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { over, Client, Message as StompMessage} from 'stompjs';
import { environment } from "../../../../environments/environment";
import { Observable } from "rxjs";
import { Message } from "src/app/model/Message";
import { User } from "src/app/model/User";
import { TokenUtilsService } from "src/app/modules/shared/services/token-utils.service";
import { UserService } from "src/app/modules/shared/services/user.service";
import { LivechatService } from "src/app/modules/shared/services/livechat.service";

const LIVECHAT_SUPPORT: string = "support";

@Component({
  selector: 'app-livechat',
  templateUrl: './livechat.component.html',
  styleUrls: ['./livechat.component.css'],
})

export class LivechatComponent {

  private stompClient : Client;

  loggedUser : User | null;
  allUsersFromMessages: User[] = [];
  profilePicture: string;
  userToChatPicture: string;

  userChat : Message[] = [];
  adminChat : Map<string, Message[]>
  messageToSend: string = "";
  userEmailToSend: string = "";
  customerSupportProfileImage: string = environment.customerSupportProfileImage;

  constructor(private livechatService: LivechatService, private tokenUtilsService: TokenUtilsService, private userService: UserService) {}
  
  ngOnInit() {
    let Sock = new SockJS(environment.apiURL + "/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);    
  }

  onConnected = () => {
      this.loggedUser = this.tokenUtilsService.getUserFromToken();
      this.userService.getProfilePicture(this.loggedUser?.email as string)
      .subscribe({
        next: (response: string) => {          
          this.profilePicture = "data:image/jpg;base64, " + response;
        }});
      
      if(this.loggedUser?.role === 'ADMIN'){
        this.stompClient.subscribe("/chatroom/public", this.onPublicMessageReceived);

        let users:Observable<User[]> = this.livechatService.getAllUsersFromMessages();
        users.subscribe(val => this.allUsersFromMessages = val );
        
        let msgs:Observable<Map<string, Message[]>> = this.livechatService.getAdminChat();
        msgs.subscribe(val => this.adminChat = new Map(Object.entries(val)));        
      }
      else{             
        this.stompClient.subscribe("/user/" + this.loggedUser?.email  + "/private", this.onPrivateMessageReceived);

        let msgs:Observable<Message[]> = this.livechatService.findAllMessagesForUser(this.loggedUser?.email as string);
        msgs.subscribe(val => this.userChat = val);        
      }    
          
  }

  onPublicMessageReceived = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);
      this.adminChat.get(this.userEmailToSend)?.push(payloadData);      
  }

  onPrivateMessageReceived = (payload: StompMessage) => {
      let payloadData = JSON.parse(payload.body);      
      this.userChat.push(payloadData);
  }

  onError = () => {
    console.log("Socket error.");    
  }

  sendPublicMessage = () => {
    if(this.stompClient){

        let chatMessage : Message = {
          senderEmail: this.loggedUser?.email as string,
          senderFirstName: this.loggedUser?.name as string,
          senderLastName: this.loggedUser?.surname as string,
          senderImage: this.loggedUser?.profileImage as string,

          receiverEmail: LIVECHAT_SUPPORT,
          receiverFirstName: "Admin",
          receiverLastName: "Support",
          receiverImage: null,
          content : this.messageToSend,
          date: new Date(),
          status: 1
        }        
        
        this.messageToSend = "";
        this.userChat.push(chatMessage);
        this.stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
        this.livechatService.persistMessage(chatMessage);
    }
  
  }

  sendPrivateMessage = () => {
    if(this.stompClient){

      let chatMessage: Message = {
        senderEmail: LIVECHAT_SUPPORT,
        senderFirstName: "Admin",
        senderLastName: "Support",
        senderImage: environment.customerSupportProfileImage,

        receiverEmail: this.userEmailToSend,
        receiverFirstName: null,
        receiverLastName: null,
        receiverImage: null,
        content: this.messageToSend,
        date: new Date(),
        status: 1
      }
      
      this.messageToSend = "";
      this.adminChat.get(this.userEmailToSend)?.push(chatMessage);
      this.stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
      this.livechatService.persistMessage(chatMessage);
    }
  }

  selectUserToChat = (email: string) => {
      this.userService.getProfilePicture(email)
      .subscribe({
        next: (response: string) => {
          
          this.userToChatPicture = "data:image/jpg;base64, " + response;
                    
        }});

      this.userEmailToSend = email;
      if (this.adminChat instanceof Map<string, Message[]>) {        
        this.userChat = this.adminChat.get(email) as Message[];
      }        
  }

}
