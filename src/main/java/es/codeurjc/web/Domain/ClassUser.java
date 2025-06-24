package es.codeurjc.web.Domain;

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

    @ManyToMany(mappedBy = "usersList", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private List<GroupClass> listOfClasses = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
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

        public List<GroupClass> getListOfClasses() {
            return listOfClasses;
        }

    public void setListOfClasses(List<GroupClass> listOfClasses) {
        this.listOfClasses = listOfClasses;
    }

    public List<Post> getListOfPosts() {
        return listOfPosts;
    }

    public void setListOfPosts(List<Post> listOfPosts) {
        this.listOfPosts = listOfPosts;
    }

}
