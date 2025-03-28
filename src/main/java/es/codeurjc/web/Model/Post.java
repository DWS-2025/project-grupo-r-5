package es.codeurjc.web.Model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.sql.Blob;

@Entity
public class Post {
    //Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postid;
    @ManyToOne(cascade = CascadeType.ALL)
    private ClassUser creator;
    private String title;
    private String description;
    @Lob
    private Blob imageFile;
    private String imagePath;

    //Comment the next line when everything fixed
    //private List<ClassUser> listOfUsers = new ArrayList<>();


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
        this.imagePath = image;
    }
    public Post() {}

    //Methods:
    /*
    public boolean removeUser(ClassUser classUser) {
        if(this.listOfUsers.contains(classUser) && this.listOfUsers.remove(classUser)) {
            classUser.setListOfPosts(null);
            return true;
        }
        return false;
    }
    */


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
