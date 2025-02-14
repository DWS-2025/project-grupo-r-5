package es.codeurjc.web.Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    //Properties:
    private Long userid;
    private String name;
    private List<Post> userPost = new ArrayList<>();
    private List<GroupClass> userClass = new ArrayList<>();


    //Constructor:
    public User(String name){
        this.name = name;
        userPost = new ArrayList<>();
        userClass = new ArrayList<>();
    }


    //Getters & setters:
        //Id
    public Long getUserid() {
        return userid;
    }
    public void setUserid(Long userid) {
        this.userid = userid;
    }

        //Name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

        //UserPosts
    public List<Post> getUserPost() {
        return userPost;
    }
    public void setUserPost(List<Post> userPost) {
        this.userPost = userPost;
    }
    public void addPost(Post post){
        this.userPost.add(post);
    }

        //UserGroupClasses
    public List<GroupClass> getUserClass() {
        return userClass;
    }
    public void setUserClass(List<GroupClass> userClass) {
        this.userClass = userClass;
    }
    public void addToClass (GroupClass groupClass){
        this.userClass.add(groupClass);
    }

}
