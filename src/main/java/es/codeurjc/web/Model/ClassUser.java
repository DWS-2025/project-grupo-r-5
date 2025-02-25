package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Post.class)
    private List<Post> listOfPosts = new ArrayList<>();

    //private List<GroupClass> userClass = new ArrayList<>();


    //Constructor:
    public ClassUser() {}

    public ClassUser(String name){
        this.name = name;
        //In the future when needed this will be uncommented
        listOfPosts = new ArrayList<>();
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

    public void addPost(Post post) {this.listOfPosts.add(post);}

        //UserPosts
    public List<Post> getListOfPosts() {
        return listOfPosts;
    }
    public void setListOfPosts(List<Post> userPost) {
        this.listOfPosts = userPost;
    }

    //In the future when needed this will be uncommented
    /*
        //UserGroupClasses
    public List<GroupClass> getUserClass() {
        return userClass;
    }
    public void setUserClass(List<GroupClass> userClass) {
        this.userClass = userClass;
    }
    */

}
