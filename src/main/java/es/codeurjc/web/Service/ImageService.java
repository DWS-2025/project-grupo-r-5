package es.codeurjc.web.Service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    protected static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");



    public String createImageFromFileName(String imageName) {
        // Verify valid extension
        if(!imageName.matches(".*\\.(jpg|jpeg|gif|png)$")){
            return "redirect:/error?message=" + URLEncoder.encode("The url is not an image resource", StandardCharsets.UTF_8);
        }

        // Generate new unique name
        String newFileName = "image_" + UUID.randomUUID() + "_" + imageName;

        // Images in a specific folder
        Path imagePath = IMAGES_FOLDER.resolve(newFileName).normalize().toAbsolutePath();
        if (!imagePath.startsWith(IMAGES_FOLDER.toAbsolutePath())) {
            return "redirect:/error?message=" + URLEncoder.encode("Invalid image path", StandardCharsets.UTF_8);
        }

        /*
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        // Verify image is not out of the folder
        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }*/

        // Verify file exists
        File file = new File(imagePath.toString());
        if (!file.exists() || !file.isFile()) {
            return "redirect:/error?message=" + URLEncoder.encode("Image file does not exist or is not valid", StandardCharsets.UTF_8);
        }

        // Move to final localization
        try {
            // If file exists, we move it to the final place
            Files.move(file.toPath(), imagePath, StandardCopyOption.ATOMIC_MOVE); // Use FileUtils.copyFile if you want to copy instead
        } catch (Exception ex) {
            logger.error("Error moving image", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't move image locally", ex);
        }

        return newFileName;
    }


    public String createImage(MultipartFile multiPartFile) {

        String originalName = multiPartFile.getOriginalFilename();

        if(!originalName.matches(".*\\.(jpg|jpeg|gif|png)$")){
            return "redirect:/error?message=" + URLEncoder.encode("The URL is not an image resource", StandardCharsets.UTF_8);
        }

        String fileName = "image_" + UUID.randomUUID() + "_" +originalName;

        //NEW
        Path imagePath = IMAGES_FOLDER.resolve(fileName).normalize().toAbsolutePath();
        if (!imagePath.startsWith(IMAGES_FOLDER.toAbsolutePath())) {
            return "redirect:/error?message=" + URLEncoder.encode("Invalid image path", StandardCharsets.UTF_8);
        }
        /*
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        */
        //END NEW

        try (InputStream inputStream = multiPartFile.getInputStream()) {
            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            logger.error("Error saving image", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save image locally", ex);
        }

        return fileName;
    }

    public Resource getImage(String imageName) {
        //NEW
        Path imagePath = IMAGES_FOLDER.resolve(imageName).normalize().toAbsolutePath();
        if (!imagePath.startsWith(IMAGES_FOLDER.toAbsolutePath())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        /*
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        */
        //END NEW
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            logger.error("Error loading image", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image", e);
        }
    }

    public void deleteImage(String imageName) {
        //NEW
        Path imagePath = IMAGES_FOLDER.resolve(imageName).normalize().toAbsolutePath();
        if (!imagePath.startsWith(IMAGES_FOLDER.toAbsolutePath())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        /*
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        */
        //END NEW
        try {
            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image does not exist");
            }
            Files.delete(imagePath);
        } catch (IOException e) {
            logger.error("Error deleting image", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image", e);
        }
    }
}

