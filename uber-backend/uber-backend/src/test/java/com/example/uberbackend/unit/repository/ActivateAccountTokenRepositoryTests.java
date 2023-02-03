package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.ActivateAccountToken;
import com.example.uberbackend.repositories.ActivateAccountTokenRepository;
import com.example.uberbackend.repositories.MessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
@Transactional
public class ActivateAccountTokenRepositoryTests {

    @Autowired
    ActivateAccountTokenRepository activateAccountTokenRepository;

    // SW-1-2019
    @Test
    public void whenFindByToken_thenReturnSuccess(){
        Optional<ActivateAccountToken> token = activateAccountTokenRepository.findByToken("random_token");

        Assertions.assertTrue(token.isPresent());
        Assertions.assertEquals(7, token.get().getUser().getId());
    }

    @Test
    public void whenFindByToken_thenDontFind(){
        Optional<ActivateAccountToken> token = activateAccountTokenRepository.findByToken("unknown_token");
        Assertions.assertTrue(token.isEmpty());
    }
}
