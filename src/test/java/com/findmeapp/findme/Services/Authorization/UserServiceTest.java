package com.findmeapp.findme.Services.Authorization;

import com.findmeapp.findme.Models.Entities.User;
import com.findmeapp.findme.Models.Enums.Role;
import com.findmeapp.findme.Repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User user;

    @BeforeEach
    void initModel(){

        user = new User();{
            user.setId(1);
            user.setEmail("test@gmail.com");
            user.setRole(Role.USER);
            user.setUsername("test");
            user.setPassword("12345678");
        }
    }

    @Test
    void createUserTest(){

        Mockito.when(repository.existsByUsername("test")).thenReturn(true);

        var result = service.createUser(user);

        Assertions.assertEquals(user, result);
    }
    @Test
    void createUserFalseExistsTest(){

        Mockito.when(repository.existsByUsername("test")).thenReturn(false);

        var result = service.createUser(user);

        Assertions.assertEquals(user, result);
    }
    @Test
    void createUserThrowExTest(){

        Mockito.when(repository.existsByUsername("test"))
                .thenThrow(new RuntimeException("Database error"));

        var result = service.createUser(user);

        Assertions.assertNull(result);
    }

    @Test
    void getByUsernameTest(){

        Optional<User> testUser = Optional.ofNullable(user);

        Mockito.when(repository.getUserByLogin("test")).thenReturn((testUser));

        var result = service.getByUsername("test");

        Assertions.assertEquals(result, user);
    }

    @Test
    void userDetailsService_Success() {
        Mockito.when(repository.getUserByLogin("test")).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = service.userDetailsService();

        var result = userDetailsService.loadUserByUsername("test");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user, result);
    }

    @Test
    void userDetailsService_UserNotFound() {
        Mockito.when(repository.getUserByLogin("test")).thenReturn(Optional.empty());

        UserDetailsService userDetailsService = service.userDetailsService();

        Assertions.assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test"));
    }

}
