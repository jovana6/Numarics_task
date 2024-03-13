package com.assignment.demoproject;

import com.assignment.demoproject.userservice.User;
import com.assignment.demoproject.userservice.UserRepository;
import com.assignment.demoproject.userservice.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testRegisterUser() {
        User user = new User("username", "passsword","ROLE");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertEquals(user, registeredUser);
    }

    @Test
    void testGetUserByUsername() {
        String username = "testUser";
        User user = new User(username, "password","ROLE");
        when(userRepository.findByUser(username)).thenReturn(Optional.of(user));

        User retrievedUser = userService.getUserByUsername(username);

        assertEquals(user, retrievedUser);

    }


}
