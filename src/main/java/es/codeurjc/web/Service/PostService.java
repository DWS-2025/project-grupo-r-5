package es.codeurjc.web.Service;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


import es.codeurjc.web.Dto.*;
import es.codeurjc.web.Repositories.PostRepository;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.rowset.serial.SerialBlob;

@Service
public class PostService {
    @Autowired
    private ImageService imageService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostMapper mapper;
    @Autowired
    private ClassUserMapper classUserMapper;

    public PostService(){}

    //Methods
    /*
    public Page<PostDTO> findAll(Pageable page) {
        Page<Post> posts = postRepository.findAll(page);
        return toDTOs(posts.getContent());
    } */

    public Page<PostDTO> findAll(Pageable page) {
        Page<Post> posts = postRepository.findAll(page);
        List<PostDTO> dtoList = posts
                .getContent()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, page, posts.getTotalElements());
    }

    public Optional<PostDTO> findById(long id){return postRepository.findById(id).map(mapper::toDTO);}

    public PostDTO getPost (long id){return toDTO(postRepository.findById(id).orElseThrow());}

    public PostDTO save(PostDTO postDTO) throws IOException {

        Post post = toDomain(postDTO);

        postRepository.save(post);

        return toDTO(post);

    }

    public PostDTO save(PostDTO postDTO, MultipartFile imageFile) throws IOException {

        Post post = toDomain(postDTO);

        // If imageFile isn't null, we use ImageService to save it
        if (!imageFile.isEmpty()) {
            post.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }

        if(post.getImagePath() == null || post.getImagePath().isEmpty()) post.setImagePath("no-image.png");
        postRepository.save(post);

        return toDTO(post);
    }

    public PostDTO save(String user, String title, String description, MultipartFile imageFile) throws Exception {

        Optional <ClassUserDTO> classUserDTO = userService.findByName(user);

        if(classUserDTO.isPresent()){

            ClassUser classUser = classUserMapper.toDomain(classUserDTO.get());

            String imagePath;

            if (!imageFile.isEmpty()) {

                imagePath = imageService.createImage(imageFile);

            } else{

                imagePath = "no-image.png";

            }


            Post newpost = new Post(classUser, title, description, imagePath, imageFile);
            postRepository.save(newpost);

            return toDTO(newpost);

        }

        return null;

    }

    public boolean checkCreator(PostDTO postDTO, ClassUserDTO classUserDTO) {
        return postDTO.creator().userid() == classUserDTO.userid();
    }

    public PostDTO edit(PostDTO updatedPostDTO, MultipartFile imageFile, long id) throws IOException {
        if (postRepository.existsById(id)) {

            Post updatedPost = toDomain(updatedPostDTO);
            updatedPost.setPostid(id);

            // If imageFile isn't null, we use ImageService to save it
            if (!imageFile.isEmpty()) {
                updatedPost.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            }

            if(updatedPost.getImagePath() == null || updatedPost.getImagePath().isEmpty()) updatedPost.setImagePath("no-image.png");
            postRepository.save(updatedPost);

            return toDTO(updatedPost);

        } else {
            throw new NoSuchElementException();
        }
    }

    public PostDTO delete(long id){
        Post post = postRepository.findById(id).orElseThrow();

        postRepository.deleteById(id);

        return toDTO(post);
    }

    public Blob getBlobImage(long id) throws IOException {
        Post post = postRepository.findById(id).orElseThrow();
        return post.getImageFile();
    }

    public void deleteImageByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (post.getImagePath() != null) {
            imageService.deleteImage(post.getImagePath());
            post.setImagePath(null);
            postRepository.save(post);
        }
    }

    public String updateImageForPost(Long postId, MultipartFile newImageFile) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        //Delete previous image if it exists
        if (post.getImagePath() != null) {
            imageService.deleteImage(post.getImagePath());
        }

        //Save new image
        String newImageUrl = imageService.createImage(newImageFile);
        post.setImagePath(newImageUrl);
        postRepository.save(post);

        return newImageUrl;
    }

    public PostDTO toDTO(Post post){
        return mapper.toDTO(post);
    }

    public Post toDomain(PostDTO postDTO){
        return mapper.toDomain(postDTO);
    }

    /*public Page<PostDTO> toDTOs(Collection<Post> posts){
        return mapper.toDTOs(posts);
    }*/

    public Page<PostDTO> toDTOs(Page<Post> postPage) {
        List<PostDTO> dtoList = postPage
                .getContent()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        //If you want to reverse the order:
        //Collections.reverse(dtoList);
        return new PageImpl<>(dtoList, postPage.getPageable(), postPage.getTotalElements());
    }

}
