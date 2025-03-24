package es.codeurjc.web.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ClassUser {
    //Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userid;
    private String name;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<GroupClass> listOfClasses = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
