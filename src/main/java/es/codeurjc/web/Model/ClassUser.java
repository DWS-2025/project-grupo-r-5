package es.codeurjc.web.Model;

import java.util.ArrayList;
import java.util.List;

public class ClassUser {
    //Properties:
    private Long userid; //More efficient
    private String name;
    private List<GroupClass> listOfClasses = new ArrayList<>();
    private List<Post> listOfPosts = new ArrayList<>();

    //Constructor:
    public ClassUser() {}
    public ClassUser(String name){
        this.name = name;
        listOfPosts = new ArrayList<>();
        listOfClasses = new ArrayList<>();
    }

    //Methods:
    public boolean addPost(Post post){
        if(this.listOfPosts.add(post)){
            post.setCreator(this);
            return true;
        }
        return false;
    }
    public boolean removePost(Post post) {
        if(this.listOfPosts.contains(post) && this.listOfPosts.remove(post)){
            post.setCreator(null);
            return true;
        }
        return false;
    }
    public boolean addClass(GroupClass groupClass){
        return this.listOfClasses.add(groupClass);
    }
    public boolean removeClass(GroupClass groupClass) {
        return this.listOfClasses.remove(groupClass);
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
    public List<Post> getListOfPosts() {
        return this.listOfPosts;
    }
    public void setListOfPosts(List<Post> userPost) {
        this.listOfPosts = userPost;
    }
        //UserGroupClasses
    public List<GroupClass> getUserClass() {
        return this.listOfClasses;
    }
    public void setUserClass(List<GroupClass> userClass) {
        this.listOfClasses = userClass;
    }

}
