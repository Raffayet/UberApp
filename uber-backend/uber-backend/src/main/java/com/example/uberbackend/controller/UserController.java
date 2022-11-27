package com.example.uberbackend.controller;


import com.example.uberbackend.dto.RegisterDto;
import com.example.uberbackend.service.EmailService;
import com.example.uberbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    @GetMapping
    public void SendEmail() throws InterruptedException {
        emailService.sendConfirmationRegistrationRequest("jovancevicjovan5@gmail.com");
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto registerDto, BindingResult result){
        try{
            String message = userService.registerUser(registerDto, result);
            if (message.equals("Success"))
                return new ResponseEntity<>(HttpStatus.CREATED);
            return ResponseEntity.badRequest().build();
        }catch (RuntimeException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "token") String token, HttpServletResponse httpServletResponse){
        userService.activateAccount(token);
        httpServletResponse.setHeader("Location", "http://localhost:4200/activatedAccount");
        httpServletResponse.setStatus(302);
    }

}
