package com.findmeapp.findme.Repositories;

import com.findmeapp.findme.Models.Entities.Photo;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoRepository {

    private final SessionFactory factory = new Configuration()
            .configure("hibernateconfig.cfg.xml")
            .addAnnotatedClass(Photo.class)
            .buildSessionFactory();


    public void Add(Photo model){

    }
    public Photo getByModel(Photo model){


        //debug
        Photo test = new Photo();
        test.setIdentityCode("111");
        test.setId(1);
        test.setCountSilhouette(2);


        return null;
    }
    public void delete(){

    }
    public void update(){

    }
}
