package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.ClassUserMapper;
import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Dto.PostMapper;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Controller
public class BlogWebController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ClassUserMapper classUserMapper;

    //Get
    @GetMapping("/blog")
    public String showBlog(Model model) {
        model.addAttribute("Posts", postService.findAll());
        return "blog";
    }

    @GetMapping("/blog/new")
    public String newPost(Model model) {
        model.addAttribute("Post", new Post()); // Empty post
        model.addAttribute("isEdit", false);
        return "post_form";
    }

    @GetMapping("/blog/{id}")
    public String showPost(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
        if(postOptional.isPresent()){
            PostDTO post = postOptional.get();
            model.addAttribute("Post", post);
            String imagefile = post.imagePath();
            if(!imagefile.matches("no-image.png")){
                model.addAttribute("ImagePresented", true);
            }
            return "show_post";
        } else {
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }
        /*try{
            PostDTO post = postService.getPost(id);
            model.addAttribute("Post", post);
            String imagefile = post.imagePath();
            if(!imagefile.matches("no-image.png")){
                model.addAttribute("ImagePresented", true);
            }
            return "show_post";
        } catch (NoSuchElementException e){
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }*/
    }

    @GetMapping("/blog/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
        if (postOptional.isPresent() && postOptional.get().imagePath() != null) {
            try {
                PostDTO post = postOptional.get();
                Blob image = postService.getBlobImage(id);
                Resource file = new InputStreamResource(image.getBinaryStream());
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(image.length()).body(file);
            } catch (SQLException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error retrieving image");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            return ResponseEntity.notFound().build();
        }

        /*try {
            PostDTO post = postService.getPost(id);
            Blob image = postService.getBlobImage(id);
            Resource file = new InputStreamResource(image.getBinaryStream());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(image.length()).body(file);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    @GetMapping("/blog/changePost/{id}")
    public String editPost(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
        try {
            if (postOptional.isPresent()){
                PostDTO post = postService.getPost(id);
                model.addAttribute("post", post);
                model.addAttribute("isEdit", true);
                return "post_form";
            } else {
                redirectAttributes.addAttribute("message", "Post not found");
                return "redirect:/error";
            }
        } catch (NoSuchElementException e) {
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }
    }

    @GetMapping("/blog/deletePost/{id}")
    public String deletePost(Model model,@PathVariable long id, RedirectAttributes redirectAttributes){
        try {
            Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
            if (postOptional.isPresent()) {
                model.addAttribute("DeletePost", true);
                postService.delete(id);
                return "deleted_post";
            } else {
                redirectAttributes.addAttribute("message", "Post not found");
                return "redirect:/error";
            }
        } catch (Exception e){
            redirectAttributes.addAttribute("message", "Error deleting post");
            return "redirect:/error";
        }
    }

    @GetMapping("/blog/{id}/deleteImage/{imageName}")
    public String deleteImage(Model model, @PathVariable String imageName, @PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("DeleteImage", true);
            imageService.deleteImage(imageName);

            Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
            if (postOptional.isPresent()) {
                PostDTO post = postOptional.get();
                PostDTO updatedPost = new PostDTO(post.postid(), post.creator(), post.title(), post.description(), "no-image.png");
                postService.save(updatedPost);
                model.addAttribute("postid", id);
                return "deleted_post";
            } else {
                redirectAttributes.addAttribute("message", "Post not found");
                return "redirect:/error";
            }
        } catch (IOException e) {
            redirectAttributes.addAttribute("message", "Error saving post after deleting image");
            return "redirect:/error";
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", "Error deleting image");
            return "redirect:/error";
        }
    }


    //Post
    @PostMapping("/blog/new")
    public String newPostProcess(Model model, @RequestParam("title") String title,
                                 @RequestParam("description") String description,
                                 @RequestParam("user") String user,
                                 @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                 RedirectAttributes redirectAttributes) throws IOException {

    // The validation of the inputs should be here
        // String validationError = postService.createAndValidatePost(title, desc, user, imagefile);
    // Add an if line with: if (validationError != null && !validationError.isEmpty()) ...

    PostDTO postDTO = postService.save(user,title,description,imagefile);

    if(postDTO == null){
        return "redirect:/error";
    }

    return "redirect:/blog/" + postDTO.postid();

    }

    @PostMapping("/blog/changePost/{id}")
    public String editPostProcess(@PathVariable long id, Model model, @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("user") String user,
                                  @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                  @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage,
                                  RedirectAttributes redirectAttributes) throws IOException {

        // Check if the post exists
        Optional <PostDTO> opPost = Optional.ofNullable(postService.getPost(id));

        if(opPost.isEmpty()){
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }

        //once we check if the post is not empty we get it

        PostDTO originalPost = opPost.get();

        // Get the creatorId

        long userId = originalPost.creator().userid();

        //Check if the creator exist and if then, get it

        Optional <ClassUserDTO> opUser = userService.findById(userId);

        if(opUser.isEmpty()){
            redirectAttributes.addAttribute("message", "User not found");
            return "redirect:/error";
        }

        ClassUserDTO userDTO = opUser.get();

        // Check if the user is the creator of the post

        if(postService.checkCreator(originalPost, userDTO)){

            PostDTO updatedPost = new PostDTO(
                    originalPost.postid(),
                    userDTO,
                    title,
                    Jsoup.parse(description).text(),
                    originalPost.imagePath()
            );

            postService.edit(updatedPost,imagefile, originalPost.postid());

            return "redirect:/blog/" + updatedPost.postid();
        }

        redirectAttributes.addAttribute("message", "The post cannot be updated");
        return "redirect:/error";

    }

}
