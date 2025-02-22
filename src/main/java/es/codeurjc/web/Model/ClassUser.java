package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ClassUser {
    //Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid; //More efficient
    private String name;

    //In the future when needed this will be uncommented
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Post> userPost = new ArrayList<>();

    //private List<GroupClass> userClass = new ArrayList<>();


    //Constructor:
    public ClassUser() {}

    public ClassUser(String name){
        this.name = name;
        //In the future when needed this will be uncommented
        userPost = new ArrayList<>();
        //userClass = new ArrayList<>();
    }

    //Methods:
    //In the future when needed this will be uncommented
    /*public void addPost(Post post){
        this.userPost.add(post);
        post.setCreator(this);
    }
    public void removePost(Post post) {
        userPost.remove(post);
        post.setCreator(null);
    }

    public void addToClass (GroupClass groupClass){
        this.userClass.add(groupClass);
    }
    public boolean joinClass(GroupClass groupClass) {
        if (!groupClass.isFull()) {
            userClass.add(groupClass);
            return groupClass.addUser(this);
        }
        return false;
    }
    public void leaveClass(GroupClass groupClass) {
        userClass.remove(groupClass);
        groupClass.removeUser(this);
    }*/

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
    //In the future when needed this will be uncommented
/*
        //UserPosts
    public List<Post> getUserPost() {
        return userPost;
    }
    public void setUserPost(List<Post> userPost) {
        this.userPost = userPost;
    }

        //UserGroupClasses
    public List<GroupClass> getUserClass() {
        return userClass;
    }
    public void setUserClass(List<GroupClass> userClass) {
        this.userClass = userClass;
    }
    */

}
