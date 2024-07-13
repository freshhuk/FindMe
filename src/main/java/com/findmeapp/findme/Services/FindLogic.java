package com.findmeapp.findme.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;


@Service
public class FindLogic {

    public int getSilhouette(MultipartFile image){
        try {
            System.out.println("Image was got " + image.getOriginalFilename());


            // Read image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            System.out.println(" image width " +  bufferedImage.getWidth()
                    + " Height " + bufferedImage.getHeight());

            //Logic save in db or check it


            //Get count of silhouette
            int countSilhouette = findSilhouette(bufferedImage);

            return countSilhouette;

        } catch (IOException ex) {
            System.out.println("Ops... " + ex);
            return 0;
        }
    }

    private int findSilhouette(BufferedImage image){
        int countSilhouette = 0;



        return 0;
    }
    private void getUploadedImage(){

    }

}
