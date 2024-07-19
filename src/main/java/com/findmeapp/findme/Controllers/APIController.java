package com.findmeapp.findme.Controllers;

import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Services.FindLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
public class APIController {

    private final FindLogic findSilhouetteService;

    @Autowired
    public APIController(FindLogic findSilhouetteService){
        this.findSilhouetteService = findSilhouetteService;
    }

    @PostMapping("/getSilhouette")
    public ResponseEntity<String> getSilhouette(MultipartFile image){

        // Data for save db
        Photo photo = new Photo();{
            photo.setFileName(image.getOriginalFilename());
            photo.setSize(image.getSize());
            photo.setFormat(image.getContentType());
            photo.setCountSilhouette(0);
        }

        System.out.println("Size image " + image.getSize() + " OriginalFile name " + image.getOriginalFilename() + " Content type" + image.getContentType());

        int count = findSilhouetteService.getSilhouette(image, photo);

        return ResponseEntity.ok().body("Cont - " + count);
    }

}
