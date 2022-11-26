package com.example.uberbackend.service;

import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.dto.UserDto;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.User;
import com.example.uberbackend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    
    private MessageRepository messageRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public List<MessageDto> findAllBySenderEmail(String senderEmail){
        List<MessageDto> retList = new ArrayList<>();

        for(Message m : this.messageRepository.findAllBySenderEmail(senderEmail)){
            retList.add(new MessageDto(m));
        }
        return retList;
    }

    public List<MessageDto> findAllByReceiverEmail(String receiverEmail){
        List<MessageDto> retList = new ArrayList<>();

        for(Message m : this.messageRepository.findAllByReceiverEmail(receiverEmail)){
            retList.add(new MessageDto(m));
        }
        return retList;
    }

    public List<MessageDto> findAllForUser(String userEmail) {
        List<MessageDto> retList = new ArrayList<>();

        for(Message m : this.messageRepository.findAllForUser(userEmail)){
            retList.add(new MessageDto(m));
        }
        return retList;
    }

    public List<UserDto> getDistinctUserFromMessages() {
        List<UserDto> retList = new ArrayList<>();

        for(User m : this.messageRepository.getDistinctUserFromMessages()){
            retList.add(new UserDto(m));
        }
        return retList;
    }
}
