package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.sql.Blob;
import java.sql.SQLException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/posts")
public class BlogAPIController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ValidateService validateService;


    //CRUD operations for Post:
    @GetMapping("/")
    public Page<PostDTO> getPosts(Pageable page) {
        return postService.findAll(page);
    }

    @GetMapping("/{id}")
    public PostDTO getPost(long id) {
        return postService.getPost(id);
    }

    @PostMapping("/")
    public PostDTO createPost(@RequestBody PostDTO postDTO) throws IOException {
        postDTO = postService.save(postDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(postDTO.postid()).toUri();
        return ResponseEntity.created(location).body(postDTO).getBody();
    }

    @PutMapping("/{id}")
    public PostDTO replacePost(@PathVariable long id, @RequestBody PostDTO updatedPostDTO) throws IOException {
        return postService.save(updatedPostDTO);
    }

    @DeleteMapping("/{id}")
    public PostDTO deletePost(@PathVariable long id) {
        return postService.delete(id);
    }

    //CRUD operations for Images:
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {

        Blob postImage = postService.getBlobImage(id);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(postImage);

    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> createPostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        URI location = fromCurrentRequest().build().toUri();
        imageService.createImage(imageFile);
        return ResponseEntity.created(location).build();

    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replacePostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        postService.updateImageForPost(id, imageFile);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deletePostImage(@PathVariable long id) throws IOException {
        postService.deleteImageByPostId(id);
        return ResponseEntity.noContent().build();
    }

}
