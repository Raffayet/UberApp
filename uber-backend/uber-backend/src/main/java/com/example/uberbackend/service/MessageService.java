package com.example.uberbackend.service;

import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.dto.UserDto;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.MessageRepository;
import com.example.uberbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.event.MouseWheelEvent;
import java.util.*;

@Service
public class MessageService {
    
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
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

    public HashMap<String, List<MessageDto>> findAllGroupedByUser() {
        HashMap<String, List<MessageDto>> map = new HashMap<>();
        List<Message> allMessages = messageRepository.findAll();
        allMessages.sort((Message m1, Message m2) -> m1.getDate().compareTo(m2.getDate()));

        for(Message m : allMessages){
            if(!Objects.equals(m.getSender().getRole().getName(), RoleType.ADMIN.toString())) {
                map.computeIfAbsent(m.getSender().getEmail(), k -> new ArrayList<>()).add(new MessageDto(m));
            }
            if(!Objects.equals(m.getReceiver().getRole().getName(), RoleType.ADMIN.toString())){
                map.computeIfAbsent(m.getReceiver().getEmail(), k -> new ArrayList<>()).add(new MessageDto(m));
            }
        }
        return map;
    }

    public void saveMessage(MessageDto dto){
        Message m = new Message();
        Optional<User> sender = userRepository.findByEmail(dto.getSenderEmail());
        Optional<User> receiver = userRepository.findByEmail(dto.getReceiverEmail());
        if(sender.isPresent() && receiver.isPresent()){
            m.setSender(sender.get());
            m.setReceiver(receiver.get());
        }
        m.setContent(dto.getContent());
        m.setDate(dto.getDate());
        m.setStatus(dto.getStatus());

        messageRepository.save(m);
    }
}
