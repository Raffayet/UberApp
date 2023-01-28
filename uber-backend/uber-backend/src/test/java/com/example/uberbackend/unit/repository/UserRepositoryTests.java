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

import javax.swing.text.html.Option;
import javax.transaction.Transactional;

import java.util.ArrayList;
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
        List<User> allUsers = userRepository.findAll();
        List<User> onlyAdmins = new ArrayList<>();

        for(User user: allUsers)
        {
            if(!user.getRole().getName().equals("ADMIN"))
            {
                onlyAdmins.add(user);
            }
        }

        List<String> userEmails = userRepository.getUserEmails();

        assertEquals(onlyAdmins.size(), userEmails.size());
    }

    @Test
    void getNotBlockedUserEmailsTest()
    {
        // When
        List<User> allUsers = userRepository.findAll();
        List<User> onlyAdminsAndNotBlocked = new ArrayList<>();

        for(User user: allUsers)
        {
            if(!user.getRole().getName().equals("ADMIN") && !user.getBlocked())
            {
                onlyAdminsAndNotBlocked.add(user);
            }
        }

        List<String> userEmails = userRepository.getNotBlockedUserEmails();

        assertEquals(onlyAdminsAndNotBlocked.size(), userEmails.size());
    }

    @Test
    void findByEmailTestSuccess()
    {
        String existingEmail = "sasalukic@gmail.com";

        Optional<User> user = userRepository.findByEmail(existingEmail);

        user.ifPresent(value -> assertEquals(value.getEmail(), existingEmail));
    }

    @Test
    void findByEmailTestFailureWrongEmail()
    {
        String email = "jajajaa@gmail.com";

        Optional<User> user = userRepository.findByEmail(email);

        assertEquals(user, Optional.empty());
    }

    @Test
    void findByEmailTestFailureEmptyEmail()
    {
        String email = "";

        Optional<User> user = userRepository.findByEmail(email);

        assertEquals(Optional.empty(), user);
    }

    @Test
    void existsByEmailTestSuccess()
    {
        String existingEmail = "sasalukic@gmail.com";

        Boolean shouldExist = userRepository.existsByEmail(existingEmail);

        assertEquals(true, shouldExist);
    }

    @Test
    void existsByEmailTestFailureWrongEmail()
    {
        String email = "jajajaa@gmail.com";

        Boolean shouldNotExist = userRepository.existsByEmail(email);

        assertEquals(false, shouldNotExist);
    }
}
