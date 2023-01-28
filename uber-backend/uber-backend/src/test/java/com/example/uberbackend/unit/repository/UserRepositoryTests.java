package com.example.uberbackend.unit.repository;

import com.example.uberbackend.model.Role;
import com.example.uberbackend.model.User;
import com.example.uberbackend.model.enums.RoleType;
import com.example.uberbackend.repositories.RoleRepository;
import com.example.uberbackend.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Test
    void getUserEmailsTest()
    {
        // When
        List<String> userEmails = userRepository.getUserEmails();

        for(String userEmail: userEmails)
        {
            Optional<User> user = userRepository.findByEmail(userEmail);
            user.ifPresent(value -> assertNotEquals(value.getRole().getName(), "ADMIN"));
        }

        assertEquals(5, userEmails.size());
    }

    @Test
    void getNotBlockedUserEmailsTest()
    {
        // When
        List<String> userEmails = userRepository.getNotBlockedUserEmails();

        for(String userEmail: userEmails)
        {
            Optional<User> user = userRepository.findByEmail(userEmail);
            if(user.isPresent())
            {
                assertNotEquals(user.get().getRole().getName(), "ADMIN");
                assertEquals(user.get().getBlocked(), false);
            }
        }

        assertEquals(4, userEmails.size());
    }
}
