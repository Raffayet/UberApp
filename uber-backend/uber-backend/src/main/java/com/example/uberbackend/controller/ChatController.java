package com.example.uberbackend.controller;

import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private MessageService messageService;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService){
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public MessageDto receivePublicMessage(@Payload MessageDto message){
        return message;
    }

    @MessageMapping("/private-message")
    public MessageDto receivePrivateMessage(@Payload MessageDto message){
        // if user wants to listen to this particular message, it needs to listen to /user/David/private e.g.

        simpMessagingTemplate.convertAndSendToUser(message.getReceiverEmail(), "/private", message);
        return message;
    }
}
