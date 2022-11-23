import "../init.js";
import { Component } from '@angular/core';
import { LivechatService } from 'src/app/services/livechat.service';
import * as SockJS from 'sockjs-client';
import { over, Client } from 'stompjs';

// interface Message{
//   senderName: string,
//   receiverName: string,
//   content: string,
//   date: string,
//   status: string
// }

@Component({
  selector: 'app-livechat',
  templateUrl: './livechat.component.html',
  styleUrls: ['./livechat.component.css']
})

export class LivechatComponent {

  private stompClient : Client;
  private userChats : Map<string, string[]>;

  onlineUsers: string[] = ["Jovan", "Petar"];
  adminChat : string[] = ["a", "a", "a", "a", "a", "a"];
  message: string;

  constructor(private livechatService: LivechatService) {}
  
  ngOnInit() {
    let Sock = new SockJS("http://localhost:8081/api/ws");
    this.stompClient = over(Sock);
    this.stompClient.connect({}, this.onConnected, this.onError);
  }

  onConnected = () => {
      console.log("\n\n\nUser is connected\n\n\n");
      // username izvadi iz tokena, ako je u pitanju user(client ili driver)
      let username = "David";

      this.stompClient.subscribe("/chatroom/public", this.onPublicMessageReceived);
      this.stompClient.subscribe("/user/" + username + "/private", this.onPrivateMessageReceived);      
  }

  onPublicMessageReceived = (payload: any) => {
      let payloadData = JSON.parse(payload.body);
      //this.adminChat.push(payloadData);
      console.log("JOCA");      
      //console.log(this.adminChat);
  }

  onPrivateMessageReceived = (payload: any) => {
      let payloadData = JSON.parse(payload.body);
      if(this.userChats.get(payloadData.senderName)){
          this.userChats.get(payloadData.senderName)?.push(payloadData);
      }
      else{
          let messages : string[] = [];
          messages.push(payloadData);

          this.userChats.set(payloadData.senderName, messages);
      }
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

        let chatMessage = {
          senderName: "David",
          receiverName: null,
          content : message,
          date: "Datum",
          status: 'UNREAD'
        }
    
        this.stompClient.send('/app/message', {}, JSON.stringify(chatMessage));
    }
  
  }

  sendPrivateMessage = () => {
    if(this.stompClient){
      let message = "Pozdrav!";

      let chatMessage = {
        senderName: "David",
        receiverName: "David",
        content : message,
        date: "Datum",
        status: 'UNREAD'
      }
  
      this.stompClient.send('/app/private-message', {}, JSON.stringify(chatMessage));
    }
  }

}
