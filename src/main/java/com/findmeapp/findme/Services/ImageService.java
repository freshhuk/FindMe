package com.findmeapp.findme.Services;

import com.findmeapp.findme.Repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final PhotoRepository repository;

    @Autowired
    public ImageService(PhotoRepository repository){
        this.repository = repository;
    }


    public String deleteAllImage(){
        try{
            repository.delete();
            return "Successful";
        } catch (Exception ex){
            System.out.println("Error with deleting all entity");
            return "Error";
        }
    }
}
