package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.awt.image.Raster;
import java.io.IOException;


@Service
public class FindLogic {

    public int getSilhouette(MultipartFile image, Photo photo){
        try {
            System.out.println("Image was got " + image.getOriginalFilename());


            // Read image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //Array with rgb pixels of image
            int[][] pixelsImage = getPixelArray(bufferedImage);

            //Raster raster = bufferedImage.getRaster();





            //Logic save in db or check it
            getUploadedImage(photo);

            //Get count of silhouette
            int countSilhouette = findSilhouette(pixelsImage);


            //Debug

            System.out.println(" image width " +  bufferedImage.getWidth()
                    + " Height " + bufferedImage.getHeight());
            System.out.println("RGB - " + bufferedImage.getRGB(3, 4)
                    + " getTileHeight " + bufferedImage.getTileHeight());



            for(int y = 0; y < pixelsImage.length; y++){
                for(int x = 0; x < pixelsImage[y].length; x++) {
                    System.out.print(pixelsImage[y][x] + " ");
                }
                System.out.println(" ");
            }

            return countSilhouette;

        } catch (IOException ex) {
            System.out.println("Ops... " + ex);
            return 0;
        }
    }

    private int findSilhouette(int[][] pixelsImage){
        int countSilhouette = 0;
        //Как вариант решение проходить по массиву пиксилей сверху в низ а потом слева в право, и просматривать
        //Обьекты которые заканчиваются по границе и заканчиватся


        return 0;
    }

    /**
     * Method for saving entity into db or get this entity from db
     * @param photo object for loading into db
     */
    private void getUploadedImage(Photo photo){

    }


    /**
     * Method for getting pixels from image
     * @param bufferedImage source image
     * @return array with pixels
     */
    private int[][] getPixelArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] pixelArray = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelArray[y][x] = bufferedImage.getRGB(x, y);
            }
        }

        return pixelArray;
    }
}
