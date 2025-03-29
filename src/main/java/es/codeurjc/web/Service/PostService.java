package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.Post;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import es.codeurjc.web.Repositories.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static es.codeurjc.web.Service.ImageService.IMAGES_FOLDER;

@Service
public class PostService {
    @Autowired
    private ImageService imageService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    private ConcurrentMap<Long,Post> posts = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1L);

    public PostService(){}

    //Methods
    public List<Post> findAll(){return postRepository.findAll();}

    public Optional<Post>findById(long id){return postRepository.findById(id);}

    public boolean exist(long id){return postRepository.existsById(id);}

    public void save(Post post, MultipartFile imageFile) throws IOException {
        // If imageFile isn't null, we use ImageService to save it
        if (imageFile != null && !imageFile.isEmpty()) {
            String path = imageService.createImage(imageFile);
            post.setImagePath(path);
        }

        if(post.getImagePath() == null || post.getImagePath().isEmpty()) post.setImagePath("no-image.png");


        postRepository.save(post);
    }

    public void delete(long id){
        postRepository.deleteById(id);
    }

    public void edit(Post post, MultipartFile imageFile, long id) throws IOException {
        //Post newP = posts.get(id);
        Post newP = postRepository.getReferenceById(id);
        if(newP == null){ //If the post is not valid
            System.out.println("Post not found\n");
        } else {
            newP.setTitle(post.getTitle());
            newP.setDescription(post.getDescription());
            newP.setCreator(post.getCreator());

            if (imageFile != null && !imageFile.isEmpty()) {
                String path = imageService.createImage(imageFile);
                post.setImagePath(path);
            }

            if(post.getImagePath() == null || post.getImagePath().isEmpty()) post.setImagePath("no-image.png");

            //We don't need to actualize the id
            //posts.put(id, newP);
            postRepository.save(newP);
            userService.addPost(newP.getPostid(), newP.getCreator().getUserid());
        }
    }


}
