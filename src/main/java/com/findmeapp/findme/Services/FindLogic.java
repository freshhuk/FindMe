package com.findmeapp.findme.Services;

import com.findmeapp.findme.Models.Entities.Photo;
import com.findmeapp.findme.Repositories.PhotoRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FindLogic {

    private final PhotoRepository repository;

    @Value("${opencv.library.path:D:\\LibraryJava\\opencv\\build\\java\\x64}")
    private String libPath;

    @Value("${opencv.library.name:opencv_java4100}")
    private String libName;

    private static final double MAX_WIDTH = 1024.0;
    private static final double MIN_CONTOUR_AREA_PERCENT = 0.005;
    private static final double CANNY_THRESHOLD_1 = 50.0;
    private static final double CANNY_THRESHOLD_2 = 150.0;

    public FindLogic(PhotoRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        System.setProperty("java.library.path", libPath);
        try {
            System.loadLibrary(libName);
            log.info("OpenCV loaded successfully: {}", libName);
        } catch (UnsatisfiedLinkError e) {
            log.error("Failed to load OpenCV. Trying fallback...", e);
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }
    }

    public int getSilhouette(MultipartFile file, Photo photo) {
        if (file == null || file.isEmpty()) return 0;

        try {
            byte[] fileBytes = file.getBytes();

            String identityCode = generateFileHash(fileBytes);
            photo.setIndentitycode(identityCode);

            Photo originPhoto = repository.getByModel(photo);
            if (originPhoto != null) {
                return originPhoto.getCountsilhouette();
            }


            MatOfByte mob = new MatOfByte(fileBytes);
            Mat originalImage = Imgcodecs.imdecode(mob, Imgcodecs.IMREAD_COLOR);
            mob.release();

            if (originalImage.empty()) {
                log.warn("Failed to decode image");
                return 0;
            }

            int count = countObjectsAdvanced(originalImage);

            originalImage.release();

            photo.setCountsilhouette(count);
            repository.Add(photo);

            return count;

        } catch (IOException e) {
            log.error("Error reading file", e);
            return 0;
        }
    }


    private int countObjectsAdvanced(Mat src) {
        List<Mat> garbageCollector = new ArrayList<>();

        try {
            Mat resized = new Mat();
            garbageCollector.add(resized);

            double scale = 1.0;
            if (src.cols() > MAX_WIDTH) {
                scale = MAX_WIDTH / src.cols();
                Imgproc.resize(src, resized, new Size(src.cols() * scale, src.rows() * scale));
            } else {
                src.copyTo(resized);
            }

            Mat gray = new Mat();
            garbageCollector.add(gray);
            Imgproc.cvtColor(resized, gray, Imgproc.COLOR_BGR2GRAY);

            //  Gaussian Blur
            Mat blurred = new Mat();
            garbageCollector.add(blurred);
            Imgproc.GaussianBlur(gray, blurred, new Size(5, 5), 0);

            //  Canny Edge Detection
            Mat edges = new Mat();
            garbageCollector.add(edges);
            Imgproc.Canny(blurred, edges, CANNY_THRESHOLD_1, CANNY_THRESHOLD_2);

            //  Morphological Closing
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
            garbageCollector.add(kernel);

            Mat closed = new Mat();
            garbageCollector.add(closed);
            Imgproc.morphologyEx(edges, closed, Imgproc.MORPH_CLOSE, kernel);

            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            garbageCollector.add(hierarchy);

            Imgproc.findContours(closed, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            int objectCount = 0;
            double imageArea = resized.cols() * resized.rows();
            double minArea = imageArea * MIN_CONTOUR_AREA_PERCENT;


            for (MatOfPoint contour : contours) {
                double area = Imgproc.contourArea(contour);

                if (area < minArea) {
                    continue;
                }

                Rect rect = Imgproc.boundingRect(contour);
                double aspectRatio = (double) rect.width / rect.height;

                if (aspectRatio > 10 || aspectRatio < 0.1) {
                     continue;
                }

                objectCount++;
                contour.release();
            }

            log.info("Found {} objects (after filtering)", objectCount);
            return objectCount;

        } catch (Exception e) {
            log.error("CV pipeline failed", e);
            return 0;
        } finally {
            for (Mat mat : garbageCollector) {
                if (mat != null) mat.release();
            }
        }
    }

    private String generateFileHash(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}