package com.findmeapp.findme.Services.Authorization;

import com.findmeapp.findme.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserServiceTest {


    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test
    void createUserTest(){

    }
}
