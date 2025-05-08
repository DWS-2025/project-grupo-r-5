package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
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

    //Get
    @GetMapping("/blog")
    public String showBlog(Model model, Pageable page) {
        model.addAttribute("Posts", postService.findAll(page));
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

        Post post = new Post();


        ClassUserDTO classUserDTO = userService.findByName(user)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        ClassUser classUser = userService.toDomain(classUserDTO);

        post.setCreator(classUser);
        post.setTitle(title);
        post.setDescription(Jsoup.parse(description).text());

        if(imagefile != null && !imagefile.isEmpty()){
            //Validate image before uploading
            String imageValidationError = validateService.validatePostWithImage(post);
            if (imageValidationError != null && !imageValidationError.isEmpty()) {
                model.addAttribute("error", imageValidationError);
                model.addAttribute("Post", post);

                redirectAttributes.addAttribute("message", imageValidationError);
                return "redirect:/error";
            }

            // Create directory if it doesn't exist
            Path imagesDir = Paths.get("images");
            if (!Files.exists(imagesDir)) {
                Files.createDirectories(imagesDir);
            }

            // Generate unique name and save
            String imageName = UUID.randomUUID() + "_" + imagefile.getOriginalFilename();
            Path imagePath = imagesDir.resolve(imageName).normalize();

            post.setImagePath(imageName);
        }

        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("Post", post);

            redirectAttributes.addAttribute("message", validationError);
            return "redirect:/error";
        }


        PostDTO postDTO = postService.toDTO(post);

        postService.save(postDTO, imagefile);
        userService.addPost(post.getPostid(), classUser.getUserid());
        userService.save(userService.toDTO(classUser));


        return "redirect:/blog/" + post.getPostid();
    }


    @PostMapping("/blog/changePost/{id}")
    public String editPostProcess(@PathVariable long id, Model model, @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("user") String user,
                                  @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                  @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage,
                                  RedirectAttributes redirectAttributes) throws IOException {

        // Check if the post exists
        Optional <PostDTO> op = Optional.ofNullable(postService.getPost(id));
        if(op.isEmpty()){
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }
        // Get the post
        PostDTO originalPost = op.get();
        // Check if the user is the creator of the post
        ClassUserDTO classUser = originalPost.creator();

        if(classUser == null || !classUser.getClass().getName().equals(user)) {
            classUser = userService.findByName(user).orElseThrow();
            if (classUser == null) {
                redirectAttributes.addAttribute("message", "User not found");
                return "redirect:/error";
            }
        }

        PostDTO updatedPost = new PostDTO(
                originalPost.postid(),
                classUser,
                title,
                Jsoup.parse(description).text(),
                originalPost.imagePath()
        );
        //Uncomment this in the 3rd phase
        String validationError = validateService.validatePost(postService.toDomain(updatedPost));
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("post", updatedPost);
            redirectAttributes.addAttribute("message", validationError);
            return "redirect:/error";
        }
        postService.edit(updatedPost, imagefile, id);

        return "redirect:/blog/" + updatedPost.postid();
    }

}
