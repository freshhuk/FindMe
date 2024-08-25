package com.findmeapp.findme.Repositories;

import com.findmeapp.findme.Models.Entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final SessionFactory factory = new Configuration()
            .configure(".cfg.xml")
            .addAnnotatedClass(User.class)
            .buildSessionFactory();

    public void Add(){

    }
}
