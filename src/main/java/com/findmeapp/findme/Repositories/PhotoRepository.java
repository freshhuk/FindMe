package com.findmeapp.findme.Repositories;

import com.findmeapp.findme.Models.Entities.Photo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class PhotoRepository {

    private final SessionFactory factory = new Configuration()
            .configure("hibernateconfig.cfg.xml")
            .addAnnotatedClass(Photo.class)
            .buildSessionFactory();


    public void Add(Photo model){

        if(model != null){
            try(Session session = factory.openSession()){
                session.beginTransaction();
                session.persist(model);
                session.getTransaction().commit();

            } catch (Exception ex){
                System.out.println("Error in Add method - " + ex);
            }
        }

    }
    public Photo getByModel(Photo model){

        try (Session session = factory.openSession()) {

            session.beginTransaction();

            Query<Photo> query = session.createQuery(
                    "from Photo where format = :format AND filename = :filename AND indentitycode = :indentitycode",
                    Photo.class
            );
            query.setParameter("format", model.getFormat());
            query.setParameter("filename", model.getFilename());
            query.setParameter("indentitycode", model.getIndentitycode());

            Photo photo = query.uniqueResult();
            session.getTransaction().commit();
            return photo;

        }catch (Exception ex) {
            System.out.println("Error method getByModel" + ex);
            return null;
        }

    }
    public void delete(){

    }
    public void update(){

    }
}
