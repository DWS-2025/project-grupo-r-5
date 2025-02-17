package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public class Post {
    //Properties:
    @JsonTypeId
    private UUID postid; //More secure
    private User creator;
    private String title;
    private String description;
    private String imageName;
    private MultipartFile imageFile;


    //Constructor:
    public Post(User user, String title, String text){
        this.creator = user;
        this.title = title;
        this.description = text;
    }

    public Post(User user, String title, String text, String image){
        this.creator = user;
        this.title = title;
        this.description = text;
        this.imageName = image;
    }

    //Methods:


    //Getters & setters:
        //Id
    public UUID getPostid() {
        return postid;
    }
    public void setPostid(UUID postid) {
        this.postid = postid;
    }

        //Creator
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }

        //Title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

        //Description
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

        //ImageName
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

        //ImageFile
    public MultipartFile getImageFile() {
        return imageFile;
    }
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
