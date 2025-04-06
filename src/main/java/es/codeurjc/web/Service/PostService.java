package es.codeurjc.web.Service;

import es.codeurjc.web.Model.Post;

import java.io.*;
import java.util.*;


import es.codeurjc.web.Repositories.PostRepository;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostService {
    @Autowired
    private ImageService imageService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public PostService(){}

    //Methods
    public Page<Post> findAll(){return postRepository.findAll(PageRequest.of(0,15));}

    public Optional<Post>findById(long id){return postRepository.findById(id);}

    public boolean exist(long id){return postRepository.existsById(id);}

    public void save(Post post) throws IOException {
        save(post, null);
    }

    public void save(Post post, MultipartFile imageFile) throws IOException {
        // If imageFile isn't null, we use ImageService to save it
        if (!imageFile.isEmpty() && imageFile != null) {
            post.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }

        if(post.getImagePath() == null || post.getImagePath().isEmpty()) post.setImagePath("no-image.png");
        postRepository.save(post);
    }

    public void delete(long id){
        postRepository.deleteById(id);
    }

    public void edit(Post post, MultipartFile imageFile, long id) throws IOException {
        Optional<Post> newP = postRepository.findById(id);
        if (newP.isPresent()) {
            Post saveP = newP.get();
            saveP.setTitle(post.getTitle());
            saveP.setDescription(post.getDescription());
            saveP.setCreator(post.getCreator());

            if (imageFile != null && !imageFile.isEmpty()) {
                String path = imageService.createImage(imageFile);
                saveP.setImagePath(path);
            }

            if(saveP.getImagePath() == null || saveP.getImagePath().isEmpty()) saveP.setImagePath("no-image.png");

            postRepository.save(saveP);
            userService.addPost(saveP.getPostid(), saveP.getCreator().getUserid());
        } else { //If the post is not valid
            System.out.println("Post not found\n");
        }
    }


}
