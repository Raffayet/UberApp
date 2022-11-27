package com.example.uberbackend.model;

import com.example.uberbackend.model.enums.MessageStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;
    private String senderEmail;
    private String receiverEmail;
    private String content;
    private LocalDateTime date;
    private MessageStatus status;

    public Message(String sender, String receiver, String content, LocalDateTime date, MessageStatus status) {
        this.senderEmail = sender;
        this.receiverEmail = receiver;
        this.content = content;
        this.date = date;
        this.status = status;
    }
}
