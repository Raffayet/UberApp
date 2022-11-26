package com.example.uberbackend.dto;

import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.MessageStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto implements Serializable {

    private String senderEmail;
    private String senderFirstName;
    private String senderLastName;
    private String receiverEmail;
    private String receiverFirstName;
    private String receiverLastName;
    private String content;
    private LocalDateTime date;
    private MessageStatus status;

    public MessageDto(Message msg){
        this.senderEmail = msg.getSender().getEmail();
        this.senderFirstName = msg.getSender().getName();
        this.senderLastName = msg.getSender().getSurname();
        this.receiverEmail = msg.getReceiver().getEmail();
        this.receiverFirstName = msg.getSender().getName();
        this.receiverLastName = msg.getSender().getSurname();
        this.content = msg.getContent();
        this.date = msg.getDate();
        this.status = msg.getStatus();
    }
}
