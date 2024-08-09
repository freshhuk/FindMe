package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class FindLogic {

    private final PhotoRepository repository;

    @Autowired
    public FindLogic(PhotoRepository repository){
        this.repository = repository;
    }

    public int getSilhouette(MultipartFile image, Photo photo){
        try {

            // Read image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //Logic save in db or check it and Get count of silhouette
            int countSilhouette = getUploadedImage(photo,bufferedImage);

           /* //Debug

            BufferedImage posterizedImage = posterize(bufferedImage, 2); // Количество уровней цвета

            // Создаем директорию для сохранения изображений, если ее нет
            File outputDir = new File("output_images");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Сохранение изображения в файл в папку проекта
            String outputFileName = "output_images/posterized_image.jpg";
            File output = new File(outputFileName);
            ImageIO.write(posterizedImage, "jpg", output);
            */
            return countSilhouette;

        } catch (IOException ex) {
            System.out.println("Ops... " + ex);
            return 0;
        }
    }

    private int findSilhouette(BufferedImage image){
        int countSilhouette = 0;

        Color bgColor = findMostPopularColor(image);
        //Как вариант решение проходить по массиву пиксилей сверху в низ а потом слева в право, и просматривать
        //Обьекты которые заканчиваются по границе и заканчиватся

        for (int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){

                Color color = new Color(image.getRGB(x, y));
                if(color != bgColor){

                }
            }
        }

        return 13;
    }

    /**
     * Method for saving entity into db or get this entity from db
     * @param photo object for loading into db
     */
    private int getUploadedImage(Photo photo, BufferedImage image){

        photo.setIndentitycode(imageIdentityCode(image));//set identity code

        Photo originPhoto = repository.getByModel(photo);
        if(originPhoto != null){
            System.out.println("Image was found");
            return originPhoto.getCountsilhouette();
        }
        else{
            photo.setCountsilhouette(findSilhouette(image));
            repository.Add(photo);
            return photo.getCountsilhouette();
        }
    }


    /**
     * Method for finding bg color from image
     * @param image - where finding bg color
     * @return - most popular color in bg
     */
    private Color findMostPopularColor(BufferedImage image){

        Map<Color, Integer> countColor = new HashMap<>();

        for (int x = 0; x < image.getWidth(); x++){

            //Top edge
            Color topEdgeColor = new Color(image.getRGB(x, 0));
            countColor.put(topEdgeColor, countColor.getOrDefault(topEdgeColor, 0) + 1);

            //Bottom edge
            Color bottomEdgeColor = new Color(image.getRGB(x, image.getHeight() - 1 ));
            countColor.put(bottomEdgeColor, countColor.getOrDefault(bottomEdgeColor, 0) + 1);
        }

        for (int y = 0; y < image.getHeight(); y++){

            //left edge
            Color leftEdgeColor = new Color(image.getRGB(0, y));
            countColor.put(leftEdgeColor, countColor.getOrDefault(leftEdgeColor, 0) + 1);

            //Right edge
            Color rightEdgeColor = new Color(image.getRGB(image.getWidth() - 1, y));
            countColor.put(rightEdgeColor, countColor.getOrDefault(rightEdgeColor, 0) + 1);
        }

        Color backgroundColor = null;
        int maxCount = 0;

        for (Map.Entry<Color, Integer> entry : countColor.entrySet()) {
            if (entry.getValue() > maxCount) {
                backgroundColor = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return backgroundColor;
    }

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
     * Method generate identity code for entity on db
     * @param image image for generate code
     * @return integer code
     */
    private String imageIdentityCode(BufferedImage image){

        StringBuilder code = new StringBuilder();

        for(int i = 0; i < image.getWidth(); i++){

            int[] rgb = getRGB(image.getRGB(i, 0));

            int numCode = rgb[0] + rgb[1] + rgb[2];

            if(code.length() > 45){
                code.delete(0, 20);
            }
            code.append(numCode);
        }

        return code.toString();
    }

}
