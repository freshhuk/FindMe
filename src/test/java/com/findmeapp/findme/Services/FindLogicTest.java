package com.findmeapp.findme.Services;


import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Repositories.PhotoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    private Photo entity;


    @BeforeEach
    void initModel(){
        entity = new Photo();{
            entity.setId(1);
            entity.setFormat("jpg");
            entity.setFilename("Test");
            entity.setIndentitycode("12345Test");
            entity.setCountsilhouette(3);
        }
    }

    /**
     * Test when we have this image in database
     */
    @Test
    void getSilhouetteByPhotoTest(){
        try{
            MultipartFile image = createMultipartFile();
            Mockito.when(repository.getByModel(entity)).thenReturn(entity);
            var result = service.getSilhouette(image, entity);

            Assertions.assertEquals(3, result);

        } catch (IOException ex){
            System.out.println("Error - " + ex);
        }
    }

    /**
     * Test when we first time to find silhouette
     */
    @Test
    void getSilhouetteTest(){
        try{
            MultipartFile image = createMultipartFile();
            Mockito.when(repository.getByModel(entity)).thenReturn(null);
            var result = service.getSilhouette(image, entity);

            Assertions.assertEquals(3, result);

        } catch (IOException ex){
            System.out.println("Error - " + ex);
        }
    }

    /**
     * Test when we get null image
     */
    @Test
    void getSilhouetteNullImageTest(){

        var result = service.getSilhouette(null, entity);

        Assertions.assertEquals(0, result);
    }

    private MultipartFile createMultipartFile() throws IOException {
        // Указываем путь к картинке на диске
        File file = new File("src/test/java/com/findmeapp/findme/TestImage/test.jpg");

        FileInputStream input = new FileInputStream(file);

        return new MockMultipartFile(
                "image",
                file.getName(),
                "image/jpeg",
                input
        );
    }
}
