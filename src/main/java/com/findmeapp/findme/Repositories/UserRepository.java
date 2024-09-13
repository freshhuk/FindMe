package com.findmeapp.findme.Repositories;

import com.findmeapp.findme.Models.Entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private final SessionFactory factory = new Configuration()
            .configure("hibernateconfig.cfg.xml")
            .addAnnotatedClass(User.class)
            .buildSessionFactory();

    public void Add(User user){
        if(user != null){
            try(Session session = factory.openSession()){
                session.beginTransaction();
                session.persist(user);
                session.getTransaction().commit();

            } catch (Exception ex){
                System.out.println("Error with operation" + ex);
            }
        } else{
            System.out.println("Model is null");
        }
    }

    public Optional<User> getUserByLogin(String username) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();
            session.getTransaction().commit();
            return Optional.ofNullable(user);
        } catch (Exception ex) {
            System.out.println("Error method getUserByLogin" + ex);
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username){

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);
            User user = query.uniqueResult();
            session.getTransaction().commit();

            return user == null;
        } catch (Exception ex) {
            System.out.println("Error method existsByUsername" + ex);
            return false;
        }
    }

}
