package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;


@Service
public class FindLogic {

    public int getSilhouette(MultipartFile image, Photo photo){
        try {

            // Read image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //Array with rgb pixels of image
            int[][] pixelsImage = getPixelArray(bufferedImage);

            int[][] converPixelImage = convertImage(pixelsImage);

            //Logic save in db or check it
            getUploadedImage(photo);

            //Get count of silhouette
            int countSilhouette = findSilhouette(converPixelImage);


            //Debug
            System.out.println("Image was got " + image.getOriginalFilename());

            System.out.println(" image width " +  bufferedImage.getWidth()
                    + " Height " + bufferedImage.getHeight());
            System.out.println("RGB - " + bufferedImage.getRGB(3, 4)
                    + " getTileHeight " + bufferedImage.getTileHeight());

            int[] test = getRGB(pixelsImage[1][1]);

            System.out.println("Red " + test[0] + " Green " + test[1] + " Blue " + test[2]);

            printImageRGB(pixelsImage);

            /*for(int y = 0; y < pixelsImage.length; y++){
                for(int x = 0; x < pixelsImage[y].length; x++) {
                    System.out.print(pixelsImage[y][x] + " ");
                }
                System.out.println(" ");
            }*/

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
    //Как вариант что бы было легче высматривать силует можно все оттенки одного цвета которые
    // размазываются на границе, взять их и сделать одним цветом к примеру контур
    // лошади черный и разтушовуется в серый для перехода а я его возьму и сделаю весь черным
    private int[][] convertImage(int[][] pixelImage){

        int[][] resultImage = new int[pixelImage.length][pixelImage[0].length];

        int alpha = 255; // Полностью непрозрачный

        for (int y = 0; y < pixelImage.length; y++) {
            for (int x = 0; x < pixelImage[y].length; x++) {

                int[] rgbPixel = getRGB(pixelImage[y][x]);

                //Какие то махинации с пикселем

                int pixel = (alpha << 24) | (rgbPixel[0] << 16) | (rgbPixel[1] << 8) | rgbPixel[2];//return in intenger pixel

                resultImage[y][x] = pixel;
            }

        }

        return resultImage;
    }



    /**
     * Get rgb colors from int pixel
     * @param pixel pixel
     * @return array with rgb colors first red, second green, third blue
     */
    private int[] getRGB(int pixel){

        int[] rgb = new int[3];// first red, second green, last blue

        rgb[0] = (pixel >> 16) & 0xff;//red
        rgb[1] = (pixel >> 8) & 0xff;//green
        rgb[2] = pixel & 0xff;//blue

        return rgb;
    }

    /**
     * Debug method for writing rgb of pixel
     * @param pixels array with pixel of image
     */
    private void printImageRGB(int[][] pixels){
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int[] rgb = getRGB(pixels[y][x]);
                System.out.print("( " + rgb[0] + " " + rgb[1] + " " + rgb[2] + " ) ");
            }
            System.out.println();
        }
    }


}
