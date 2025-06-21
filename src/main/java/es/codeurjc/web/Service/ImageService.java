package es.codeurjc.web.Service;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.sql.Blob;
import java.util.UUID;
import java.nio.file.Path;


@Service
public class ImageService{

    protected static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "images");

    public Blob imageFileFromPath(String imagePath) throws IOException {
        Path imageFullPath = IMAGES_FOLDER.resolve(imagePath);
        File imageFile = imageFullPath.toFile();

        if (!imageFile.exists()) {
            throw new FileNotFoundException("No se encontró la imagen: " + imageFullPath.toString());
        }

        InputStream is = new FileInputStream(imageFile);
        return BlobProxy.generateProxy(is, imageFile.length());
    }

    public String createImage(MultipartFile multiPartFile) {

        String originalName = multiPartFile.getOriginalFilename();

        if(!originalName.matches(".*\\.(jpg|jpeg|gif|png)")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The url is not an image resource");
        }

        String fileName = "image_" + UUID.randomUUID() + "_" +originalName;

        Path imagePath = IMAGES_FOLDER.resolve(fileName);
        try {
            multiPartFile.transferTo(imagePath);
        } catch (Exception ex) {
            System.err.println(ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't save image locally", ex);
        }

        return fileName;
    }

    public Resource getImage(String imageName) {
        Path imagePath = IMAGES_FOLDER.resolve(imageName);
        try {
            return new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't get local image");
        }
    }

    public void deleteImage(String image_url) {
        String[] tokens = image_url.split("/");
        String image_name = tokens[tokens.length -1 ];

        try {
            IMAGES_FOLDER.resolve(image_name).toFile().delete();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete local image");
        }
    }

}

