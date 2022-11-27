package com.example.uberbackend.service;

import com.example.uberbackend.dto.MessageDto;
import com.example.uberbackend.dto.UserDto;
import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.MessageRepository;
import com.example.uberbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MessageService {
    
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private final String LIVECHAT_SUPPORT = "support";
    
    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageDto> findAllForUser(String userEmail) {
        List<MessageDto> retList = new ArrayList<>();

        for(Message m : this.messageRepository.findAllForUser(userEmail)){
            if(m.getSenderEmail().equals(LIVECHAT_SUPPORT)){
                String email = m.getReceiverEmail();
                Optional<User> u = userRepository.findByEmail(email);
                u.ifPresent(user -> retList.add(new MessageDto(m, user, false)));
            }else{
                String email = m.getSenderEmail();
                Optional<User> u = userRepository.findByEmail(email);
                u.ifPresent(user -> retList.add(new MessageDto(m, user, true)));
            }
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
            if(!Objects.equals(m.getSenderEmail(), LIVECHAT_SUPPORT)) {
                Optional<User> u = userRepository.findByEmail(m.getSenderEmail());
                u.ifPresent(user -> map.computeIfAbsent(m.getSenderEmail(), k -> new ArrayList<>()).add(new MessageDto(m, user, true)));
            }
            if(!Objects.equals(m.getReceiverEmail(), LIVECHAT_SUPPORT)){
                Optional<User> u = userRepository.findByEmail(m.getReceiverEmail());
                u.ifPresent(user -> map.computeIfAbsent(m.getReceiverEmail(), k -> new ArrayList<>()).add(new MessageDto(m, user, true)));
            }
        }
        return map;
    }

    public void saveMessage(MessageDto dto){
        if(dto.getSenderEmail().equals(LIVECHAT_SUPPORT)){
            Optional<User> receiver = userRepository.findByEmail(dto.getReceiverEmail());
            receiver.ifPresent(user -> messageRepository.save(new Message(dto.getSenderEmail(), user.getEmail(), dto.getContent(), dto.getDate(), dto.getStatus())));
        }
        else{
            Optional<User> sender = userRepository.findByEmail(dto.getSenderEmail());
            sender.ifPresent(user -> messageRepository.save(new Message(user.getEmail(), dto.getReceiverEmail(), dto.getContent(), dto.getDate(), dto.getStatus())));
        }
    }
}
