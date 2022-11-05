package com.example.uberbackend.service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendConfirmationAsync(String email, String token) throws MailException {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("Primer slanja emaila pomoću Spring taska");
        String link = "http://localhost:8080/confirmation?token=" + token;
        mail.setText("Pozdrav hvala što pratiš ISA, aktiviraj svoj account na " + link + ".");

        mailSender.send(mail);
    }

    @Async
    public void sendConfirmationRegistrationRequest(String email) throws MailException, InterruptedException {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setSubject("Response for registration request");
        mail.setText("Hello, your registration request has been accepted. You can use our application now. Good Luck!");

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
