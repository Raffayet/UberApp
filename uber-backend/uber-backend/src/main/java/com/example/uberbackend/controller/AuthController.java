package com.example.uberbackend.controller;

import com.example.uberbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path = "/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshAccessToken(request, response);
    }

    @GetMapping("/logged/user")
    public void getLoggedUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.getLoggedUser(request, response);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
