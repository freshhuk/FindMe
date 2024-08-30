package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.User;
import com.findmeapp.findme.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public User createUser(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("A user with the same name already exists");
        } else {
           repository.Add(user);
        }
        return user;
    }

}
