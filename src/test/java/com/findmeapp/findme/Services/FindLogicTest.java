package com.findmeapp.findme.Services;


import com.findmeapp.findme.Repositories.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
@ExtendWith(MockitoExtension.class)
public class FindLogicTest {

    @InjectMocks
    private FindLogic service;

    @Mock
    private PhotoRepository repository;




    @Test
    void getSilhouetteTest() throws IOException{

        var result = service.getSilhouette(test);
    }
}
