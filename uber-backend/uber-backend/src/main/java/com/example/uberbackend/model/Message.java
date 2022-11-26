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
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
    private String content;
    private LocalDateTime date;
    private MessageStatus status;

//    @Override
//    public int compareTo(Message message) {
//        return getDate().compareTo(message.getDate());
//    }
}
