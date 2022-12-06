package com.example.uberbackend.repositories;
import com.example.uberbackend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository  extends JpaRepository<Message, Long> {

    @Query("SELECT c.tokens FROM Client c WHERE c.email = ?1")
    double getTokensByEmail(String email);
}
