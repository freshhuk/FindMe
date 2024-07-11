package com.findmeapp.findme.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

    @GetMapping("/getSiluet")
    public int getSiluet(){

        return 0;
    }
}
