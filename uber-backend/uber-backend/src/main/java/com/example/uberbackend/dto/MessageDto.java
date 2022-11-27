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
    private String senderFirstName = "Admin";
    private String senderLastName = "support";
    private String receiverEmail;
    private String receiverFirstName = "Admin";
    private String receiverLastName = "support";
    private String content;
    private LocalDateTime date;
    private MessageStatus status;

    public MessageDto(Message msg){
        this.senderEmail = msg.getSenderEmail();
        this.receiverEmail = msg.getReceiverEmail();
        this.content = msg.getContent();
        this.date = msg.getDate();
        this.status = msg.getStatus();
    }

    public MessageDto(Message msg, User u, boolean sender) {
        this.senderEmail = msg.getSenderEmail();
        this.receiverEmail = msg.getReceiverEmail();
        this.content = msg.getContent();
        this.date = msg.getDate();
        this.status = msg.getStatus();

        if(sender){
            this.senderFirstName = u.getName();
            this.senderLastName = u.getSurname();
        }else{
            this.receiverFirstName = u.getName();
            this.receiverLastName = u.getSurname();
        }
    }
}
