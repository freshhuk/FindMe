package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Repositories.PhotoRepository;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

@Service
public class FindLogic {

    static {
        System.setProperty("java.library.path", "D:\\opencv\\build\\java\\x64");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    private final PhotoRepository repository;

    @Autowired
    public FindLogic(PhotoRepository repository) {
        this.repository = repository;
    }

    public int getSilhouette(MultipartFile image, Photo photo) {
        try {

            // Read image
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

            //Logic save in db or check it and Get count of silhouette
            int countSilhouette = getUploadedImage(photo, bufferedImage);


            return countSilhouette;

        } catch (IOException ex) {
            System.out.println("Ops... " + ex);
            return 0;
        }
    }

    private int findSilhouette(BufferedImage sourse) {
// Находим цвет фона
        Color backgroundColor = findMostPopularColor(sourse);

        // Преобразуем BufferedImage в Mat
        Mat image = bufferedImageToMat(sourse);

        // Преобразуем изображение в градации серого
        Mat grayscale = new Mat();
        Imgproc.cvtColor(image, grayscale, Imgproc.COLOR_BGR2GRAY);

        // Создаем бинарное изображение на основе цвета фона
        Mat binaryImage = new Mat(grayscale.size(), CvType.CV_8UC1);

        for (int y = 0; y < grayscale.rows(); y++) {
            for (int x = 0; x < grayscale.cols(); x++) {
                Color pixelColor = new Color(sourse.getRGB(x, y));
                if (pixelColor.equals(backgroundColor)) {
                    binaryImage.put(y, x, 0); // Фон — черный
                } else {
                    binaryImage.put(y, x, 255); // Объекты — белые
                }
            }
        }

        // Поиск контуров
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(binaryImage, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // Визуализация контуров
        /*Mat drawing = Mat.zeros(binaryImage.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(drawing, contours, i, new Scalar(0, 255, 0), 2);
        }
*/

        // Сохранение изображения с контурами
        Imgcodecs.imwrite("output_images/contours.png", binaryImage);

        // Количество силуэтов
        int silhouetteCount = contours.size();
        System.out.println("Количество силуэтов: " + silhouetteCount);

        return silhouetteCount;
    }

    /**
     * Method for saving entity into db or get this entity from db
     *
     * @param photo object for loading into db
     */
    private int getUploadedImage(Photo photo, BufferedImage image) {

        photo.setIndentitycode(imageIdentityCode(image, photo));//set identity code

        Photo originPhoto = repository.getByModel(photo);
        if (originPhoto != null) {
            System.out.println("Image was found");
            return originPhoto.getCountsilhouette();
        } else {
            photo.setCountsilhouette(findSilhouette(image));
            repository.Add(photo);
            return photo.getCountsilhouette();
        }
    }



    /**
     * Method for finding bg color from image
     *
     * @param image - where finding bg color
     * @return - most popular color in bg
     */
    private Color findMostPopularColor(BufferedImage image) {

        Map<Color, Integer> countColor = new HashMap<>();

        for (int x = 0; x < image.getWidth(); x++) {

            //Top edge
            Color topEdgeColor = new Color(image.getRGB(x, 0));
            countColor.put(topEdgeColor, countColor.getOrDefault(topEdgeColor, 0) + 1);

            //Bottom edge
            Color bottomEdgeColor = new Color(image.getRGB(x, image.getHeight() - 1));
            countColor.put(bottomEdgeColor, countColor.getOrDefault(bottomEdgeColor, 0) + 1);
        }

        for (int y = 0; y < image.getHeight(); y++) {

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

    private Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }


    /**
     * Get rgb colors from int pixel
     *
     * @param pixel pixel
     * @return array with rgb colors first red, second green, third blue
     */
    private int[] getRGB(int pixel) {

        int[] rgb = new int[3];// first red, second green, last blue

        rgb[0] = (pixel >> 16) & 0xff;//red
        rgb[1] = (pixel >> 8) & 0xff;//green
        rgb[2] = pixel & 0xff;//blue

        return rgb;
    }

    /**
     * Method generate identity code for entity on db
     *
     * @param image image for generate code
     * @return integer code
     */
    private String imageIdentityCode(BufferedImage image, Photo photo) {


        StringBuilder code = new StringBuilder();

        for (int i = 0; i < image.getWidth(); i++) {

            int[] rgb = getRGB(image.getRGB(i, 0));

            int numCode = rgb[0] + rgb[1] + rgb[2];

            if (code.length() > 45) {
                code.delete(0, 20);
            }
            code.append(numCode);
        }

        String sha256Hex = DigestUtils.sha256Hex(photo.getFilename());

        code.append(sha256Hex);
        code.delete(0, 15).delete(55, 75);

        return code.toString();
    }

}
