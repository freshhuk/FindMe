package com.findmeapp.findme.Controllers;

import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Services.FindLogic;
import com.findmeapp.findme.Services.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class APIControllerTest {

    @InjectMocks
    private APIController controller;

    @Mock
    private  FindLogic findSilhouetteService;
    @Mock
    private  ImageService imageService;

    @Test
    void getSilhouetteTest() {
        try {
            MultipartFile image = createMultipartFile();



            Mockito.when(findSilhouetteService.getSilhouette(any(MultipartFile.class), any(Photo.class)))
                    .thenReturn(3);

            ResponseEntity<String> result = controller.getSilhouette(image);

            Assertions.assertEquals("Cont - 3", result.getBody());
            Assertions.assertEquals(200, result.getStatusCode().value());

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    @Test
    void getSilhouetteTest_WithNullImage() {
        ResponseEntity<String> result = controller.getSilhouette(null);

        Assertions.assertEquals("image null", result.getBody());
        Assertions.assertEquals(400, result.getStatusCode().value());
    }

    @Test
    void deleteAllPhotoErrorTest(){

        Mockito.when(imageService.deleteAllImage()).thenReturn("Error");

        ResponseEntity<String> result = controller.deleteAllPhoto();
        Assertions.assertEquals(result.getBody(), "Error with deleting");
        Assertions.assertEquals(400, result.getStatusCode().value());

    }
    @Test
    void deleteAllPhoto(){

        Mockito.when(imageService.deleteAllImage()).thenReturn("Successful");

        ResponseEntity<String> result = controller.deleteAllPhoto();
        Assertions.assertEquals(result.getBody(), "All entity was deleted");
        Assertions.assertEquals(200, result.getStatusCode().value());

    }
    private MultipartFile createMultipartFile() throws IOException {
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
