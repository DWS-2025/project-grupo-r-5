package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.hibernate.engine.jdbc.BlobProxy;
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
import java.nio.file.StandardCopyOption;
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
            if(imagefile == null || imagefile.matches("no-image.png")){
                model.addAttribute("ImagePresented", false);
            }else{
                model.addAttribute("ImagePresented", true);
            }
            return "show_post";
        } else {
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }
    }

    ///////////////////////
    //WE SHOULD CHANGE THIS METHOD FOR IT TO RETURN A STRING, SO IT GIVES BACK ERROR OR THE IMAGE VIEW HTML:
    @GetMapping("/blog/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id, RedirectAttributes redirectAttributes) throws SQLException {
        Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
        if (postOptional.isPresent() && postOptional.get().imagePath() != null) {
            try {
                PostDTO post = postOptional.get();
                Blob image = postService.getBlobImage(id);
                Resource file = new InputStreamResource(image.getBinaryStream());
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").contentLength(image.length()).body(file);
            } catch (SQLException e) {
                ResponseEntity.status(500).body("Error retrieving image");
                redirectAttributes.addAttribute("message", "Error retrieving image");
                //return "redirect:/error";
                return ResponseEntity.status(500).body("Error retrieving image");
            } catch (IOException e) {
                redirectAttributes.addAttribute("message", "Run time exception");
                throw new RuntimeException(e);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }
    /////////////////

    @GetMapping("/blog/changePost/{id}")
    public String editPost(Model model, @PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
        if (postOptional.isPresent()) {
            PostDTO post = postOptional.get();
            ClassUserDTO user = userService.getLoggedUser();
            if (!canEditOrDeletePost(user, post)) {
                redirectAttributes.addAttribute("message", "Unauthorized to edit post");
                return "redirect:/error";
            }
            model.addAttribute("post", post);
            model.addAttribute("isEdit", true);
            return "post_form";
        }
        else {
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }
    }

    @GetMapping("/blog/deletePost/{id}")
    public String deletePost(Model model,@PathVariable long id, RedirectAttributes redirectAttributes){
        try {
            Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
            if (postOptional.isPresent()) {
                PostDTO post = postOptional.get();
                ClassUserDTO loggedUser = userService.getLoggedUser();

                if (!canEditOrDeletePost(loggedUser, post)) {
                    redirectAttributes.addAttribute("message", "Unauthorized to delete post");
                    return "redirect:/error";
                }

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
            Optional<PostDTO> postOptional = Optional.ofNullable(postService.getPost(id));
            if (postOptional.isPresent()) {
                PostDTO post = postOptional.get();

                // Verificar que el usuario actual sea el creador del post
                ClassUserDTO loggedUser = userService.getLoggedUser();
                if (!canEditOrDeletePost(loggedUser, post)) {
                    redirectAttributes.addAttribute("message", "Unauthorized to delete image");
                    return "redirect:/error";
                }

                // Borrar imagen
                model.addAttribute("DeleteImage", true);
                imageService.deleteImage(imageName);

                // Actualizar post sin imagen
                PostDTO updatedPost = new PostDTO(post.postid(), post.creator(), post.title(), post.description(), "no-image.png");
                postService.save(updatedPost);

                model.addAttribute("postid", id);
                return "deleted_post";
            } else {
                redirectAttributes.addAttribute("message", "Post no encontrado.");
                return "redirect:/error";
            }
        } catch (IOException e) {
            redirectAttributes.addAttribute("message", "Error al guardar el post tras eliminar la imagen.");
            return "redirect:/error";
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", "Error eliminando la imagen.");
            return "redirect:/error";
        }
    }


    //Post
    @PostMapping("/blog/new")
    public String newPostProcess(Model model, @RequestParam("title") String title,
                                 @RequestParam("description") String description,
                                 @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                 RedirectAttributes redirectAttributes) throws IOException {

        Post post = new Post();

        ClassUserDTO user = userService.getLoggedUser();
        ClassUser classUser = userService.findEntityById(user.userid()).orElseThrow();

        post.setCreator(userService.toDomain(user));
        post.setTitle(title);
        //post.setDescription(Jsoup.parse(description).text());
        post.setDescription(description);

        if(imagefile != null && !imagefile.isEmpty()){
            // Create directory if it doesn't exist
            Path imagesDir = Paths.get("images");
            if (!Files.exists(imagesDir)) {
                Files.createDirectories(imagesDir);
            }

            // Generate unique name and save
            String imageName = UUID.randomUUID() + "_" + imagefile.getOriginalFilename();
            Path imagePath = imagesDir.resolve(imageName).normalize();

            post.setImagePath(imageName);

            //Validate image before uploading
            String imageValidationError = validateService.validatePostWithImage(post, imagefile);
            if (imageValidationError != null && !imageValidationError.isEmpty()) {
                model.addAttribute("error", imageValidationError);
                model.addAttribute("Post", post);

                redirectAttributes.addAttribute("message", imageValidationError);
                return "redirect:/error";
            }
        }

        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("Post", post);

            redirectAttributes.addAttribute("message", validationError);
            return "redirect:/error";
        }

        PostDTO postDTO = postService.toDTO(post);

        long postid = postService.save(postDTO, imagefile).postid();
        userService.addPost(post.getPostid(), user.userid());
        userService.save(user);

        return "redirect:/blog/" + postid;
    }


    @PostMapping("/blog/changePost/{id}")
    public String editPostProcess(@PathVariable long id, Model model,
                                  @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                  @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage,
                                  RedirectAttributes redirectAttributes) throws IOException {

        Optional<PostDTO> op = Optional.ofNullable(postService.getPost(id));
        if (op.isEmpty()) {
            redirectAttributes.addAttribute("message", "Post not found");
            return "redirect:/error";
        }

        PostDTO originalPost = op.get();
        ClassUserDTO loggedUser = userService.getLoggedUser();

        if (!canEditOrDeletePost(loggedUser, originalPost)) {
            redirectAttributes.addAttribute("message", "Unauthorized to edit post");
            return "redirect:/error";
        }

        PostDTO updatedPost = new PostDTO(
                originalPost.postid(),
                originalPost.creator(),
                title,
                description,
                originalPost.imagePath()
        );

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
    private boolean canEditOrDeletePost(ClassUserDTO user, PostDTO post) {
        if (user == null) return false;

        boolean isOwner = user.userid() == post.creator().userid();
        boolean isAdmin = user.roles() != null && user.roles().contains("ADMIN");

        return isOwner || isAdmin;
    }
}
