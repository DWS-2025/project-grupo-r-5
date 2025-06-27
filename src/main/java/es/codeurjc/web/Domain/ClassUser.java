package es.codeurjc.web.Domain;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ClassUser {

    //Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userid;

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();


    @ManyToMany(mappedBy = "usersList", fetch = FetchType.LAZY)
    private List<GroupClass> listOfClasses = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Post> listOfPosts = new ArrayList<>();

    //Constructor:
    public ClassUser() {}
    public ClassUser(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles != null ? roles : new ArrayList<>();
        this.listOfClasses = new ArrayList<>();
        this.listOfPosts = new ArrayList<>();
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
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String name) {
        this.username = name;
    }
        //Password
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {this.password = password;}
        //Roles
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
    public void addRole(String role) {
        this.roles.add(role);
    }
        //Classes
    public List<GroupClass> getListOfClasses() {
            return listOfClasses;
        }
    public void setListOfClasses(List<GroupClass> listOfClasses) {
        this.listOfClasses = listOfClasses;
    }
        //Posts
    public List<Post> getListOfPosts() {
        return listOfPosts;
    }
    public void setListOfPosts(List<Post> listOfPosts) {
        this.listOfPosts = listOfPosts;
    }

}
