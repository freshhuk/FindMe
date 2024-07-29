package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;


@Service
public class FindLogic {

    public int getSilhouette(MultipartFile image, Photo photo){
        try {

            // Read image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //Array with rgb pixels of image
            int[][] pixelsImage = getPixelArray(bufferedImage);

            //Logic save in db or check it
            getUploadedImage(photo);

            //Get count of silhouette
            int countSilhouette = findSilhouette(pixelsImage);


            //Debug

            //BufferedImage posterizedImage = posterize(bufferedImage, 2); // Количество уровней цвета

            BufferedImage posterizedImage = binarize(bufferedImage); // Количество уровней цвета

            // Создаем директорию для сохранения изображений, если ее нет
            File outputDir = new File("output_images");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Сохранение изображения в файл в папку проекта
            String outputFileName = "output_images/posterized_image.jpg";
            File output = new File(outputFileName);
            ImageIO.write(posterizedImage, "jpg", output);



            System.out.println("Image was got " + image.getOriginalFilename());

            System.out.println(" image width " +  bufferedImage.getWidth()
                    + " Height " + bufferedImage.getHeight());
            System.out.println("RGB - " + bufferedImage.getRGB(3, 4)
                    + " getTileHeight " + bufferedImage.getTileHeight());

            int[] test = getRGB(pixelsImage[1][1]);

            System.out.println("Red " + test[0] + " Green " + test[1] + " Blue " + test[2]);

            printImageRGB(pixelsImage);

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
    private BufferedImage posterize(BufferedImage image, int levels) {

        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        int factor = 256 / levels;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));

                int r = (color.getRed() / factor) * factor;
                int g = (color.getGreen() / factor) * factor;
                int b = (color.getBlue() / factor) * factor;

                Color newColor = new Color(r, g, b);
                result.setRGB(x, y, newColor.getRGB());
            }
        }

        return result;
    }

    public BufferedImage binarize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage result = new BufferedImage(width, height, image.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));

                // Convert to grayscale
                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;

                // Apply threshold
                int threshold = 128; // You can adjust the threshold value if needed
                int binaryColor = gray >= threshold ? 255 : 0;
                Color newColor = new Color(binaryColor, binaryColor, binaryColor);

                result.setRGB(x, y, newColor.getRGB());
            }
        }

        return result;
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
