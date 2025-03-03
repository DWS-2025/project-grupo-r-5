package es.codeurjc.web.Controller;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


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
        model.addAttribute("Posts", new Post()); // Empty post
        model.addAttribute("isEdit", false);
        return "post_form";
    }

    @GetMapping("/blog/{id}")
    public String showPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        if(postService.exist(id)){
            model.addAttribute("Posts", post);
            model.addAttribute("CreatorName", post.getCreatorName());
            model.addAttribute("title", post.getTitle());
            model.addAttribute("text", post.getDescription());
            String imagefile = post.getImageName();
            if(imagefile != null && !imagefile.isEmpty()){
                model.addAttribute("ImagePresented", imagefile);
            }
            return "show_post";
        } else {
            return "redirect:/error?message=" + URLEncoder.encode("Post not found", StandardCharsets.UTF_8); //If error
        }
    }

    @GetMapping("/blog/changePost/{id}")
    public String editPost(Model model, @PathVariable("id") long id) {
        Post post = postService.findById(id);
        if (postService.exist(id)) {
            //
            model.addAttribute("title", post.getTitle());
            model.addAttribute("description", post.getDescription());
            model.addAttribute("user", post.getCreatorName());
            //

            model.addAttribute("post", post);
            model.addAttribute("isEdit", true);
        } else {
            return "redirect:/error?message=" + URLEncoder.encode("Post not found", StandardCharsets.UTF_8);
            //return "redirect:/blog";
        }
        return "post_form";
    }

    @GetMapping("/blog/deletePost/{id}")
    public String deletePost(Model model,@PathVariable long id){
        try {
            postService.delete(id);
            return "deleted_post";
        } catch (Exception e){
            //If error
            return "redirect:/error?message=" + URLEncoder.encode("Error deleting post", StandardCharsets.UTF_8);
        }
    }

    /*
    @GetMapping("/blog/{id}/image")
    public ResponseEntity<Resource> getImage(@PathVariable Long id) {
        Post post = postService.findById(id);
        if (post == null || post.getImageName() == null) {
            return ResponseEntity.notFound().build();
        }
        Path imagePath = Paths.get("uploads/" + post.getImageName());
        Resource image = new UrlResource(imagePath.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
    */

    //Post
    @PostMapping("/blog/new")
    public String newPostProcess(Model model, @RequestParam("title") String title,
                                 @RequestParam("text") String description,
                                 @RequestParam("user") String user,
                                 @RequestParam(value = "imagefile", required = false) MultipartFile imagefile) throws IOException {

        Post post = new Post();

        ClassUser classUser = userService.findByName(user);
        if (classUser == null){
            classUser = new ClassUser(user);
            userService.save(classUser);
        }
        post.setCreator(classUser);

        post.setTitle(title);
        post.setDescription(Jsoup.parse(description).text()); //For clean format

        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("post", post);
            return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8); //If error
        }

        postService.save(post, imagefile);
        userService.addPost(post.getPostid(), classUser.getUserid());
        classUser.addPost(post);
        userService.save(classUser);

        return "redirect:/blog/" + post.getPostid(); //Redirect to created post
    }

    @PostMapping("/blog/changePost/{id}")
    public String editPostProcess(@PathVariable long id, Model model, @RequestParam("title") String title,
                                  @RequestParam("text") String description,
                                  @RequestParam("user") String user,
                                  @RequestParam(value = "imagefile", required = false) MultipartFile imagefile,
                                  @RequestParam(value = "deleteImage", defaultValue = "false") boolean deleteImage) throws IOException {

        Post post = postService.findById(id);
        if(!postService.exist(id)){
            return "redirect:/error?message=" + URLEncoder.encode("Post does not exists", StandardCharsets.UTF_8); //If there's error
        }

        //
        ClassUser classUser = post.getCreator();
        if(classUser == null){
            classUser = new ClassUser(user);
        }
        post.setCreator(classUser);
        //

        post.setTitle(title);
        post.setDescription(description);

        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("post", post);
            return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8); //If there's error
        } else {
            if (deleteImage) {
                postService.edit(post, null, id);
            } else {
                postService.edit(post, imagefile, id);
            }
        }

        return "redirect:/blog/" + post.getPostid(); //Redirect to edited post
    }

    /*New redirection to error Controller:
    return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8);
    * return "redirect:/error?message=" + URLEncoder.encode("Error deleting post", StandardCharsets.UTF_8);
    */

    /*Old redirection to error Controller:
     * throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting post");
     *
     * or else:
     * throw new CustomException("Error deleting post");
     * */
}
