package com.example.uberbackend.controller;


import com.example.uberbackend.service.EmailService;
import com.example.uberbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public void SendEmail() throws InterruptedException {
        emailService.sendConfirmationRegistrationRequest("jovancevicjovan5@gmail.com");
    }

}
