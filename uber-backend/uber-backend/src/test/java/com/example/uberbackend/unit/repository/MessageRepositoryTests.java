package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.User;
import com.example.uberbackend.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

@SpringBootTest
@Transactional
public class MessageRepositoryTests {
    @Autowired
    MessageRepository messageRepository;

    @Test
    void findAllBySenderEmailSuccessTest(){
        String senderEmail = "sasalukic@gmail.com";
        List<Message> messageList = messageRepository.findAllBySenderEmail(senderEmail);

        assertEquals(2, messageList.size());

        assertThat( messageList, contains(
                hasProperty("senderEmail", is("sasalukic@gmail.com")),
                hasProperty("senderEmail", is("sasalukic@gmail.com"))
        ));
    }

    @Test
    void findAllBySenderEmailNoMessageTest(){
        String senderEmail = "client@gmail.com";
        List<Message> messageList = messageRepository.findAllBySenderEmail(senderEmail);

        assertEquals(0, messageList.size());
    }

    @Test
    void findAllByReceiverEmailSuccessTest(){
        String senderEmail = "sasalukic@gmail.com";
        List<Message> messageList = messageRepository.findAllByReceiverEmail(senderEmail);

        assertEquals(1, messageList.size());

        assertThat( messageList, contains(
                hasProperty("receiverEmail", is("sasalukic@gmail.com"))
        ));
    }

    @Test
    void findAllByReceiverEmailNoMessageTest(){
        String senderEmail = "client@gmail.com";
        List<Message> messageList = messageRepository.findAllByReceiverEmail(senderEmail);

        assertEquals(0, messageList.size());
    }

    @Test
    void findAllForUserSuccessTest(){
        String userEmail = "sasalukic@gmail.com";
        List<Message> messageList = messageRepository.findAllForUser(userEmail);

        assertEquals(3, messageList.size());

        assertThat( messageList, contains(
                hasProperty("senderEmail", is("sasalukic@gmail.com")),
                hasProperty("receiverEmail", is("sasalukic@gmail.com")),
                hasProperty("senderEmail", is("sasalukic@gmail.com"))
        ));

    }

    @Test
    void findAllForUserNoMessageTest(){
        String userEmail = "bad";
        List<Message> messageList = messageRepository.findAllForUser(userEmail);

        assertEquals(0, messageList.size());
    }

    @Test
    void getDistinctUserFromMessagesSuccessTest(){
        List<String> users = messageRepository.getDistinctUserFromMessages();

        assertEquals(1, users.size());
        assertEquals(users.get(0), "sasalukic@gmail.com");

    }
}
