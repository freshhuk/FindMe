package com.findmeapp.findme.Controllers;

import com.findmeapp.findme.Services.FindLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class APIController {

    private final FindLogic findSilhouetteService;

    @Autowired
    public APIController(FindLogic findSilhouetteService){
        this.findSilhouetteService = findSilhouetteService;
    }

    @GetMapping("/getSilhouette")
    public ResponseEntity<String> getSilhouette(MultipartFile image){

        int count = findSilhouetteService.getSilhouette(image);
        return ResponseEntity.ok().body("Cont - " + count);
    }

}
