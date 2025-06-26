package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.*;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import es.codeurjc.web.Service.GroupClassService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    //Services:
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService classUserService;
    @Autowired
    private GroupClassService groupClassService;
    @Autowired
    private ValidateService validateService;
    @Autowired
    private PostRepository postRepository;


    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        return "admin";
    }

    @GetMapping("/admin/posts")
    public String showPosts(Model model, Pageable page) {
        model.addAttribute("posts", postService.findAll(page));
        return "adminPosts";
    }

    //Page it:
    @GetMapping("/admin/users")
    public String showUsers(Model model, Pageable page) {
        model.addAttribute("users", classUserService.findAllFullUser(page));
        return "adminUsers";
    }

    //Page it:
    @GetMapping("/admin/groupClasses")
    public String showGroupClasses(Model model, Pageable page) {
        model.addAttribute("GroupClasses", groupClassService.findAllFullClass(page));
        return "adminGroupClasses";
    }

    @GetMapping("/admin/groupClasses/delete-{id}")
    public String deleteGroupClass(@PathVariable long id, Model model) {
        model.addAttribute("type","GroupClass");
        model.addAttribute("types","groupClasses");
        model.addAttribute("name",groupClassService.findById(id).get().classname());
        model.addAttribute("id",groupClassService.findById(id).get().classid());
        return "adminRemoveConfirmation";
    }

    @PostMapping("/admin/groupClasses/delete-{id}")
    public String deleteGroupClassConfirmed(@PathVariable long id, RedirectAttributes redirectAttributes) {
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);

        if (groupClass.isPresent()) {
            List<ClassUserBasicDTO> usersList = new ArrayList<>(groupClass.get().usersList());

            if (!usersList.isEmpty()) {
                for (ClassUserBasicDTO user : usersList) {
                    long userId = user.userid();
                    userService.removeGroupClass(id, userId);
                }
            }
            // Now we eliminate the class
            groupClassService.delete(id);
        } else {
            redirectAttributes.addAttribute("message", "Error deleting class: Class not found.");
            return "redirect:/error";
        }
        return "redirect:/admin/groupClasses";
    }

    @GetMapping("/admin/groupClasses/new")
    public String NewGroupClass(Model model) {
        model.addAttribute("GroupClass", new GroupClass());
        return "groupClassForm";
    }

    @PostMapping("/class/new")
    public String createClass(
            @RequestParam String classname,
            @RequestParam String day,
            @RequestParam String time_init,
            @RequestParam int duration,
            @RequestParam String instructor,
            @RequestParam int maxCapacity) {

        // CCreate the new class using the constructor and its properties
        GroupClass newClass = new GroupClass(
                validateService.cleanInput(classname),
                DayOfWeek.valueOf(day.toUpperCase()),  // Convert the String a DayOfWeek
                LocalTime.parse(time_init),             // Convert the String a LocalTime
                duration,
                validateService.cleanInstructor(instructor),
                maxCapacity
        );
        GroupClassBasicDTO newgpClassBasicDTO = groupClassService.toBasicDTO(newClass);
        groupClassService.save(newgpClassBasicDTO);

        return "redirect:/admin/groupClasses";  // Redirect to a success page
    }

    @GetMapping("/admin/users/delete-{id}")
    public String deleteUser(@PathVariable long id, Model model) {
        model.addAttribute("type","User");
        model.addAttribute("types","users");
        model.addAttribute("name",userService.findById(id).get().username());
        model.addAttribute("id",userService.findById(id).get().userid());
        return "adminRemoveConfirmation";
    }

    @PostMapping("/admin/users/delete-{id}")
    public String deleteUserConfirmed(@PathVariable long id, RedirectAttributes redirectAttributes) {
        Optional<ClassUserDTO> user = userService.findById(id);

        if(user.isPresent()){
            userService.deleteUserClasses(id);
        } else {
            redirectAttributes.addAttribute("message", "Error deleting user: User not found.");
            return "redirect:/error";
        }
        userService.delete(id);

        return "redirect:/admin/users";
    }


    @GetMapping("/admin/posts/delete-{id}")
    public String deletePost(@PathVariable long id, Model model) {
        model.addAttribute("type","Post");
        model.addAttribute("types","posts");
        model.addAttribute("name",postService.findById(id).get().title());
        model.addAttribute("id",postService.findById(id).get().postid());
        return "adminRemoveConfirmation";
    }


    @PostMapping("/admin/posts/delete-{id}")
    public String deletePostConfirmed(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional <PostDTO> post = postService.findById(id);

        if (post.isPresent()) {
            postService.delete(post.get().postid());
        }
        else {
            redirectAttributes.addAttribute("message", "Error deleting post: Post not found.");
            return "redirect:/error";
        }
        return "redirect:/admin/posts";
    }
}
