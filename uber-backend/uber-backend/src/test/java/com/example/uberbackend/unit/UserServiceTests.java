package com.example.uberbackend.unit;

import com.example.uberbackend.exception.RideNotFoundException;
import com.example.uberbackend.exception.UserNotFoundException;
import com.example.uberbackend.model.User;
import com.example.uberbackend.repositories.UserRepository;
import com.example.uberbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class UserServiceTests {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    // CheckIfUserIsBlocked - SW-1-2019
    @Test
    public void whenIsUserBlocked_thenReturnTrue() {
        String email = "jovancevic@gmail.com";

        User u = new User();
        u.setBlocked(true);
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u));

        boolean isBlocked = userService.checkIfUserIsBlocked(email);
        Assertions.assertTrue(isBlocked);
    }

    @Test
    public void whenIsUserBlocked_thenReturnFalse(){
        String email = "jovancevic@gmail.com";

        User u = new User();
        u.setBlocked(false);
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(u));

        boolean isBlocked = userService.checkIfUserIsBlocked(email);
        Assertions.assertFalse(isBlocked);
    }

    @Test
    public void givenNonexistentUser_whenIsUserBlocked_thenThrowUserNotFoundException(){
        String email = "jovancevic@gmail.com";

        User u = new User();
        u.setBlocked(false);
        Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.checkIfUserIsBlocked(email));
    }
}
