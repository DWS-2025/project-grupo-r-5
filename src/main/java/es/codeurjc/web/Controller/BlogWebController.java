package es.codeurjc.web.Controller;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
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
    public String showPost(@PathVariable Long id, Model model) {
        Optional<Post> post = postService.findById(id);
        if(post.isPresent()){
            model.addAttribute("Post", post.get());
            model.addAttribute("CreatorName", post.get().getCreatorName());
            model.addAttribute("title", post.get().getTitle());
            model.addAttribute("description", post.get().getDescription());
            String imagefile = post.get().getImagePath();
            if(!imagefile.matches("no-image.png")){
                model.addAttribute("ImagePresented", true);
            }
            return "show_post";
        } else {
            return "redirect:/error?message=" + URLEncoder.encode("Post not found", StandardCharsets.UTF_8);
        }
    }
    @GetMapping("/blog/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Post> op = postService.findById(id);

        if(op.isPresent()) {
            Post post = op.get();
            Resource poster = imageService.getImage(post.getImagePath());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(poster);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
    }

    @GetMapping("/blog/changePost/{id}")
    public String editPost(Model model, @PathVariable("id") long id) {
        Optional<Post> op = postService.findById(id);
        if (op.isPresent()) {

            Post post = op.get();

            System.out.println("Post encontrado: " + post);
            System.out.println("Descripción del post: " + post.getDescription());
            //
            model.addAttribute("title", post.getTitle());
            model.addAttribute("description", post.getDescription());
            model.addAttribute("creatorName", post.getCreatorName());
            //

            model.addAttribute("post", post);
            model.addAttribute("isEdit", true);
        } else {
            System.out.println("Post no encontrado para ID: " + id);
            return "redirect:/error?message=" + URLEncoder.encode("Post not found", StandardCharsets.UTF_8);
            //return "redirect:/blog";
        }
        return "post_form";
    }

    @GetMapping("/blog/deletePost/{id}")
    public String deletePost(Model model,@PathVariable long id){
        try {
            model.addAttribute("DeletePost", true);
            postService.delete(id);
            return "deleted_post";
        } catch (Exception e){
            return "redirect:/error?message=" + URLEncoder.encode("Error deleting post", StandardCharsets.UTF_8);
        }
    }

    @GetMapping("/blog/{id}/deleteImage/{imageName}")
    public String deleteImage(Model model, @PathVariable String imageName, @PathVariable long id) {
        try{
            model.addAttribute("DeleteImage", true);
            imageService.deleteImage(imageName);
            postService.findById(id).ifPresent(post -> {post.setImagePath("no-image.png");});
            model.addAttribute("postid", id);
            return "deleted_post";
        } catch (Exception e) {
            return "redirect:/error?message=" + URLEncoder.encode("Error deleting image", StandardCharsets.UTF_8);
        }
    }


    //Post
    @PostMapping("/blog/new")
    public String newPostProcess(Model model, @RequestParam("title") String title,
                                 @RequestParam("description") String description,
                                 @RequestParam("user") String user,
                                 @RequestParam(value = "imagefile", required = false) MultipartFile imagefile) throws IOException {

        Post post = new Post();

        ClassUser classUser = userService.findByName(user).orElseGet(() -> {
            ClassUser newUser = new ClassUser(user);
            return userService.save(newUser);
        });

        if (classUser == null){
            classUser = new ClassUser(user);
            userService.save(classUser);
        }
        post.setCreator(classUser);

        post.setTitle(title);
        post.setDescription(Jsoup.parse(description).text());

        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("Post", post);
            return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8);
        }

        //Validate image before uploading
        if(imagefile != null && !imagefile.isEmpty()){
            Path imagesDir = Paths.get("images");
            if (!Files.exists(imagesDir)) {
                Files.createDirectories(imagesDir);
            }

            // Generate unique name and save
            String imageName = UUID.randomUUID() + "_" + imagefile.getOriginalFilename();
            Path imagePath = imagesDir.resolve(imageName).normalize();

            System.out.println("Nombre de archivo: " + imagefile.getOriginalFilename());
            System.out.println("Tamaño del archivo: " + imagefile.getSize());
            System.out.println("Ruta absoluta: " + imagesDir.toAbsolutePath());



            post.setImagePath(imageName);
        }

        postService.save(post, imagefile);
        userService.addPost(post.getPostid(), classUser.getUserid());
        classUser.addPost(post);
        userService.save(classUser);

        return "redirect:/blog/" + post.getPostid();
    }

    @PostMapping("/blog/changePost/{id}")
    public String editPostProcess(@PathVariable long id, Model model, @RequestParam("title") String title,
                                  @RequestParam("description") String description,
                                  @RequestParam("user") String user,
                                  @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                  @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage) throws IOException {

        Optional <Post> op = postService.findById(id);
        if(!op.isPresent()){
            return "redirect:/error?message=" + URLEncoder.encode("Post does not exists", StandardCharsets.UTF_8);
        }
        Post post = op.get();
        //
        ClassUser classUser = post.getCreator();
        if(classUser == null || !classUser.getName().equals(user)){
            classUser = userService.findByName(user).orElseGet(() -> {
                ClassUser newUser = new ClassUser(user);
                userService.save(newUser);
                return newUser;
            });
        }
        post.setCreator(classUser);
        //

        post.setTitle(title);
        post.setDescription(Jsoup.parse(description).text());

        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("post", post);
            return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8);
        }
            postService.edit(post, imagefile, id);


        return "redirect:/blog/" + post.getPostid();
    }

}
