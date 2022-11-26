package com.example.uberbackend.controller;

import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.dto.UserDto;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @GetMapping("get-all-by-sender")
    public ResponseEntity<List<MessageDto>> getAllBySenderEmail(@RequestParam("email") String senderEmail){
        List<MessageDto> retList;
        try{
            retList = messageService.findAllBySenderEmail(senderEmail);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(retList);
    }

    @GetMapping("get-all-by-receiver")
    public ResponseEntity<List<MessageDto>> getAllByReceiverEmail(@RequestParam("email") String receiverEmail){
        List<MessageDto> retList;
        try{
            retList = messageService.findAllByReceiverEmail(receiverEmail);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(retList);
    }

    @GetMapping("get-all-for-user")
    public ResponseEntity<List<MessageDto>> getAllForUser(@RequestParam("email") String userEmail){
        List<MessageDto> retList;
        try{
            retList = messageService.findAllForUser(userEmail);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(retList);
    }

    @GetMapping("get-users")
    public ResponseEntity<List<UserDto>> getDistinctUsersFromMessages(@RequestParam("email") String userEmail){
        List<UserDto> retList;
        try{
            retList = messageService.getDistinctUserFromMessages();
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(retList);
    }
}
