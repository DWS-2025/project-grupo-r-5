package es.codeurjc.web.Model;


public class Post {
    //Properties:
    private long postid;
    private ClassUser creator;
    private String title;
    private String description;
    private String imageName;
    private String imagePath;


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
        return creator.getName();
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

        //ImageName
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

        //ImagePath
    public String getImagePath() {return imagePath;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
}
