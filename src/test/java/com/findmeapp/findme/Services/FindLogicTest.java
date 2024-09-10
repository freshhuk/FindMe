package com.findmeapp.findme.Services;


import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Repositories.PhotoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FindLogicTest {

    @InjectMocks
    private FindLogic service;

    @Mock
    private PhotoRepository repository;


    /**
     * Test when we have this image in database
     */
    @Test
    void getSilhouetteByPhotoTest(){

        Photo entity = new Photo();{
            entity.setId(1);
            entity.setFormat("jpg");
            entity.setFilename("Test");
            entity.setIndentitycode("12345Test");
            entity.setCountsilhouette(3);
        }
        Mockito.when(repository.getByModel(entity)).thenReturn(entity);
        var result = service.getSilhouette(null, entity);

        Assertions.assertEquals(0, result);
    }
}
