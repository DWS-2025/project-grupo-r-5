package es.codeurjc.web.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {

    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    public String createImage(MultipartFile multiPartFile) {

        String originalName = multiPartFile.getOriginalFilename();

        if(!originalName.matches(".*\\.(jpg|jpeg|gif|png)")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");
        }

        String fileName = "image_" + UUID.randomUUID() + "_" +originalName;

        //NEW
        Path imagePath = IMAGES_FOLDER.resolve(fileName).normalize().toAbsolutePath();
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        //END NEW

        try {
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }

        return fileName;
    }

    public Resource getImage(String imageName) {
        //NEW
        Path imagePath = IMAGES_FOLDER.resolve(imageName).normalize().toAbsolutePath();
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        //END NEW
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image");
        }
    }

    public void deleteImage(String imageName) {
        //String[] tokens = image_url.split("/");
        //String image_name = tokens[tokens.length -1 ];
        //NEW
        Path imagePath = IMAGES_FOLDER.resolve(imageName).normalize().toAbsolutePath();
        String pathImage = imagePath.toString();
        String pathFolder = IMAGES_FOLDER.toAbsolutePath().toString();

        if (!pathImage.startsWith(pathFolder)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image path");
        }
        //END NEW
        try {
            Files.deleteIfExists(imagePath);
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image");
        }
    }
}

