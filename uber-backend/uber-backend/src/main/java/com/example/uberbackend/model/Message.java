package com.example.uberbackend.model;

import com.example.uberbackend.model.enums.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class Message {
    private String senderName;
    private String receiverName;
    private String content;
    private String date;
    private MessageStatus status;
}
