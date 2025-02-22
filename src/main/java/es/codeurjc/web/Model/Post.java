package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Entity
public class Post {
    //Properties:
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postid; //More secure
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private ClassUser creator;
    private String title;
    private String description;
    private String imageName;
    @Lob
    private byte[] imageFile;
    //private MultipartFile imageFile;


    //Constructor:
    public Post(ClassUser classUser, String title, String text){
        this.creator = classUser;
        this.title = title;
        this.description = text;
    }

    public Post(ClassUser classUser, String title, String text, String image){
        this.creator = classUser;
        this.title = title;
        this.description = text;
        this.imageName = image;
    }

    public Post() {}

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
    public ClassUser getCreator() {
        return creator;
    }
    public void setCreator(ClassUser creator) {
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
    /*
    public MultipartFile getImageFile() {
        return imageFile;
    }
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }*/
}
