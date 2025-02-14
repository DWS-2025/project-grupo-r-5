package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.Set;

@Component
@SessionScope
public class User {
    //Properties:
    @JsonTypeId
    private Long userid; //More efficient
    private String name;
    private Set<Post> userPost = new HashSet<>(); //to avoid duplicates
    private Set<GroupClass> userClass = new HashSet<>();


    //Constructor:
    public User() {}

    public User(String name){
        this.name = name;
        userPost = new HashSet<>();
        userClass = new HashSet<>();
    }

    //Methods:
    public void addPost(Post post){
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
    public Set<Post> getUserPost() {
        return userPost;
    }
    public void setUserPost(Set<Post> userPost) {
        this.userPost = userPost;
    }

        //UserGroupClasses
    public Set<GroupClass> getUserClass() {
        return userClass;
    }
    public void setUserClass(Set<GroupClass> userClass) {
        this.userClass = userClass;
    }
}
