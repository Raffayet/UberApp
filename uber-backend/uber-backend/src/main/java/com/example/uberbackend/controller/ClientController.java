package com.example.uberbackend.controller;

import com.example.uberbackend.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping("get-tokens")
    public ResponseEntity<?> getAmountOfTokens(@RequestParam String email){
        try{
            double amount = clientService.getTokensByEmail(email);
            return ResponseEntity.ok(amount);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add-tokens")
    public ResponseEntity<?> addTokens(@RequestBody String email){
        try{
            double amount = clientService.getTokensByEmail(email);
            return ResponseEntity.ok(amount);
        }catch(Exception ex){
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
