package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/posts")
public class BlogAPIController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ValidateService validateService;


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


}
