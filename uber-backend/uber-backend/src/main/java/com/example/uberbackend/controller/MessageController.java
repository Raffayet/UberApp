package com.example.uberbackend.controller;

import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.dto.UserDto;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
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
    public ResponseEntity<List<UserDto>> getDistinctUsersFromMessages(){
        List<UserDto> retList;
        try{
            retList = messageService.getDistinctUserFromMessages();
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(retList);
    }

    @GetMapping("get-admin-chat")
    public ResponseEntity<HashMap<String, List<MessageDto>>> getAdminChat(){
        HashMap<String, List<MessageDto>> map;
        try{
            map = messageService.findAllGroupedByUser();
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(map);
    }

    @PostMapping("save")
    public ResponseEntity<String> saveMessage(@RequestBody MessageDto message){
        try{
            messageService.saveMessage(message);
        }catch(Exception ex){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Success!");
    }
}
