package com.example.uberbackend.repositories;

import com.example.uberbackend.model.Message;
import com.example.uberbackend.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT r FROM Message r WHERE r.sender.email = ?1 order by r.date")
    List<Message> findAllBySenderEmail(String senderEmail);

    @Query("SELECT r FROM Message r WHERE r.receiver.email = ?1 order by r.date")
    List<Message> findAllByReceiverEmail(String receiverEmail);

    @Query("SELECT r FROM Message r WHERE r.receiver.email = ?1 or r.sender.email = ?1 order by r.date")
    List<Message> findAllForUser(String userEmail);

    @Query("SELECT DISTINCT r.sender FROM Message r WHERE r.receiver.role = '3'")
    List<User> getDistinctUserFromMessages();
}
