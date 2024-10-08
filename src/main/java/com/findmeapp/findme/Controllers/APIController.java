package com.findmeapp.findme.Controllers;

import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Services.FindLogic;
import com.findmeapp.findme.Services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
public class APIController {

    private final FindLogic findSilhouetteService;
    private final ImageService imageService;

    @Autowired
    public APIController(FindLogic findSilhouetteService, ImageService imageService){
        this.findSilhouetteService = findSilhouetteService;
        this.imageService = imageService;
    }

    @PostMapping("/getSilhouette")
    public ResponseEntity<String> getSilhouette(MultipartFile image){

        if(image != null){
            // Data for save db
            Photo photo = new Photo();{
                photo.setFilename(image.getOriginalFilename());
                photo.setFormat(image.getContentType());
                photo.setCountsilhouette(0);
            }

            int count = findSilhouetteService.getSilhouette(image, photo);
            return ResponseEntity.ok().body("Cont - " + count);
        }
        return ResponseEntity.badRequest().body("image null");
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllPhoto(){

        String result = imageService.deleteAllImage();
        if(result.equals("Successful")){
            return ResponseEntity.ok().body("All entity was deleted");
        }
        return ResponseEntity.badRequest().body("Error with deleting");
    }
}
