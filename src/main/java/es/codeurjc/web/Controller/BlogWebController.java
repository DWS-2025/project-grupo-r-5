package es.codeurjc.web.Controller;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
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
        model.addAttribute("post", new Post()); // Empty post
        model.addAttribute("isEdit", false);
        return "post_form";
    }

    /*@GetMapping("/blog/changePost/{id}")
    public String editPost(Model model, @PathVariable("id") UUID id) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isPresent()) {
            model.addAttribute("post", optionalPost.get());
            model.addAttribute("isEdit", true);
        } else {
            return "redirect:/blog";
        }
        return "post_form";
    }*/

    /*@GetMapping("/blog/deletePost/{id}")
    public String deletePost(Model model,@PathVariable UUID id){
        try {
            postService.delete(id);
            return "deleted_post";
        } catch (Exception e){
            //If error
            return "redirect:/error?message=" + URLEncoder.encode("Error deleting post", StandardCharsets.UTF_8);
        }
    }
*/
    //Post
    /*@PostMapping("/blog/new")
    public String newPostProcess(Model model, Post post, MultipartFile imagefile, String user) throws IOException {
        ClassUser classUser = userService.findByName(user).orElseGet(() -> {
            ClassUser newUser = new ClassUser(user);
            return userService.save(newUser);
        });

        post.setCreator(classUser);
        //post.setCreatorName(classUser.getName());

        String validationError = validateService.validatePost(post);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            model.addAttribute("post", post);
            return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8); //If error
        } else {
            postService.save(post, imagefile);
            userService.addPost(post, classUser.getUserid());
            classUser.addPost(post);
            userService.save(classUser);
        }

        return "redirect:/blog/" + post.getPostid(); //Redirect to created post
    }*/

   /* @PostMapping("/blog/changePost/{id}")
    public String editPostProcess(@PathVariable UUID id, Model model, Post post, MultipartFile imagefile, String user,
                                  @RequestParam boolean deleteImage) throws IOException {

        ClassUser classUser = new ClassUser(user);
        post.setCreator(classUser);

        String validationError = validateService.validatePost(post);
        if (validationError != null) {
            model.addAttribute("error", validationError);
            model.addAttribute("post", post);
            return "redirect:/error?message=" + URLEncoder.encode(validationError, StandardCharsets.UTF_8); //If there's error
        } else {
            if (deleteImage) {
                postService.editPost(post, null, id);
            } else {
                postService.editPost(post, imagefile, id);
            }
        }

        return "redirect:/blog/" + post.getPostid(); //Redirect to edited post
    }
*/
    /*Redirection to error Controller:
    * return "redirect:/error?message=" + URLEncoder.encode("Error deleting post", StandardCharsets.UTF_8);
     */

}
