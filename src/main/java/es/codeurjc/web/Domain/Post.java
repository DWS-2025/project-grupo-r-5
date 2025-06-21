package es.codeurjc.web.Domain;


import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

@Entity
public class Post {
    //Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private ClassUser creator;
    private String title;
    private String description;
    @Lob
    private Blob imageFile;
    private String imagePath;

    //Constructor:
    public Post(ClassUser classUser, String title, String text){
        this.creator = classUser;
        this.title = title;
        this.description = text;
    }
    public Post(ClassUser classUser, String title, String text, String image, MultipartFile imageFile) throws Exception {
        this.creator = classUser;
        this.title = title;
        this.description = text;
        this.imagePath = image;
        this.imageFile = new SerialBlob(imageFile.getBytes());
    }
    public Post() {}


    //Getters & setters:
        //Id
    public long getPostid() {
        return postid;
    }
    public void setPostid(long postid) {
        this.postid = postid;
    }

        //Creator
    public ClassUser getCreator() {
        return creator;
    }
    public String getCreatorName() {
        return (creator != null) ? creator.getName() : "Unknown user";
    }
    public void setCreator(ClassUser creator) {
        this.creator = creator;
    }
    public void setCreatorName(String username) {this.creator.setName(username);}

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

        //ImageFile
    public Blob getImageFile() {
        return imageFile;
    }
    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    //ImagePath
    public String getImagePath() {return imagePath;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
}
