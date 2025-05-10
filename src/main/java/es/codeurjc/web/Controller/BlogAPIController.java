package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

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



}
