package com.example.uberbackend.service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendConfirmationAsync(String email, String token) throws MailException {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("Activate account");
        String link = "http://localhost:8081/api/user/activate?token=" + token;
        mail.setText("You have successfully registered on Uber App, to Log In please activate account here: " + link + ".");
        try{
        mailSender.send(mail);
        }catch (RuntimeException e){
            throw new IllegalStateException("failed to send email");
        }
    }

    @Async
    public void sendConfirmationRegistrationRequest(String email) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("Account activated");
        mail.setText("Hello, you have successfully activated your account. You can use our application now. Good Luck!");

        mailSender.send(mail);

    }
    @Async
    public void sendRejectionRegistrationRequest(String email, String message) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("Response for registration request");
        mail.setText("Hello, your registration request has been denied with the following explanation.\n" + message);

        mailSender.send(mail);
    }
}
