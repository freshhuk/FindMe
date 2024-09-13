package com.findmeapp.findme.Services;

import com.findmeapp.findme.Repositories.PhotoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @InjectMocks
    private ImageService service;

    @Mock
    private PhotoRepository repository;


    @Test
    void deleteAllImageTest(){

        var result = service.deleteAllImage();

        Assertions.assertEquals("Successful", result);
    }


}
