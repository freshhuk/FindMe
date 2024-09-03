package com.findmeapp.findme.Services.Authorization;

import com.findmeapp.findme.Models.Entities.User;
import com.findmeapp.findme.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public User createUser(User user) {
        try{
            if(user != null && repository.existsByUsername(user.getUsername())){
                repository.Add(user);
            }
            return user;
        } catch (Exception ex){
            System.out.println("Error with createUser " + ex);
            return null;
        }
    }


    /**
     * Getting user by username
     *
     * @return user
     */
    public User getByUsername(String username) {
        return repository.getUserByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    /**
     * Getting user by username
     * Needed for Spring Security
     *
     * @return user
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Getting current user
     *
     * @return current user
     */
    public User getCurrentUser() {
        // Getting username from Spring Security context
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

}
