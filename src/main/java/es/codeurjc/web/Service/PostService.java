package es.codeurjc.web.Service;

import es.codeurjc.web.Model.Post;

import java.io.IOException;
import java.util.*;

import es.codeurjc.web.repository.ClassUserRepository;
import es.codeurjc.web.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ClassUserRepository classUserRepository;
    @Autowired
    private EntityManager entityManager;

    //Methods
    public boolean exist(UUID id){return postRepository.existsById(id);}

    public Post findPostById(UUID id){return postRepository.findById(id).orElseThrow();}

    public List<Post> findAll(){return new ArrayList<>(postRepository.findAll());}

    public Optional<Post> findById(UUID id) {
        return postRepository.findById(id);
    }

    public List<Post> findByIds(List<UUID>ids){
        List<Post> posts = new ArrayList<>();
        for(UUID id : ids){
            posts.add(postRepository.findById(id).orElseThrow());
        }
        return posts;
    }

    //In the future this won't be commented
    /*public List<Post> findAll(ClassUser user) {
        StringBuilder queryBuilder = new StringBuilder("SELECT p.*, u.name AS user_name FROM post p");

        if (user != null) {
            queryBuilder.append(" INNER JOIN class_user u ON p.user_id = u.id WHERE u.id = :userId");
        }

        Query query = entityManager.createNativeQuery(queryBuilder.toString(), Post.class);

        if (user != null) {
            query.setParameter("userId", user.getUserid());
        }

        return query.getResultList();
    }

    public Optional<Post> findByExample(Post postExample) {
        Example<Post> example = Example.of(postExample);
        return postRepository.findOne(example);
    }*/

    public Post save(Post post){
        if (post.getPostid() == null) {
            post.setPostid(UUID.randomUUID());
        }

        if (post.getImageName() == null || post.getImageName().isEmpty()) {
            post.setImageName("no-image.png");
        }

        return postRepository.save(post);
    }

    /*
    public Post save(Post post, MultipartFile imageField) throws IOException {

        if (imageField != null && !imageField.isEmpty()) {
            post.setImageName(imageField.getOriginalFilename());

            post.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        }

        if (post.getImageName() == null || post.getImageName().isEmpty()) {
            post.setImageName("no-image.png");
        }

        post.setPostid(UUID.randomUUID());
        return postRepository.save(post);
    }
    */

    public void delete(UUID id){
        postRepository.deleteById(id);
    }

    /*
    public void editPost(Post post, MultipartFile imageField, long id) throws IOException{

        if (imageField != null && !imageField.isEmpty()) {
            post.setImageName(imageField.getOriginalFilename());
            post.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        }

        else if (post.getImageName() == null || post.getImageName().isEmpty()) {
            Post existingProduct = postRepository.findById(id).orElseThrow();
            post.setImageFile(existingProduct.getImageFile());
            post.setImageName(existingProduct.getImageName());
        }

        postRepository.save(post);
    }

    */

}
