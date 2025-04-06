package es.codeurjc.web.Controller;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import org.springframework.data.domain.Page;
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

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        return "admin";
    }
    @GetMapping("/admin/posts")
    public String showPosts(Model model, Pageable pageable) {
        Page<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "adminPosts";
    }

    //Page it:
    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        model.addAttribute("users", classUserService.findAll());
        return "adminUsers";
    }

    //Page it:
    @GetMapping("/admin/groupClasses")
    public String showGroupClasses(Model model) {
        model.addAttribute("GroupClasses", groupClassService.findAll());
        return "adminGroupClasses";
    }
    @GetMapping("/admin/groupClasses/delete-{id}")
    public String deleteGroupClass(@PathVariable long id, Model model) {
        model.addAttribute("groupClass",groupClassService.findById(id));
        return "adminRemoveClassConfirmation";
    }
    @PostMapping("/admin/groupClasses/delete-{id}")
    public String deleteGroupClassConfirmed(@PathVariable long id) {
        Optional<GroupClass> groupClass = groupClassService.findById(id);

        if (groupClass != null) {
            List<ClassUser> usersList = new ArrayList<>(groupClass.get().getUsersList());

            if (!usersList.isEmpty()) {
                for (ClassUser user : usersList) {
                    long userId = user.getUserid();
                    userService.removeGroupClass(id, userId);
                }
            }

            // Now we eliminate the class
            groupClassService.delete(id);
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
                classname,
                DayOfWeek.valueOf(day.toUpperCase()),  // Convert the String a DayOfWeek
                LocalTime.parse(time_init),             // Convert the String a LocalTime
                duration,
                instructor,
                maxCapacity
        );

        groupClassService.save(newClass);

        return "redirect:/admin/groupClasses";  // Redirect to a success page
    }

    @GetMapping("/admin/users/delete-{id}")
    public String deleteUser(@PathVariable long id, Model model) {
        model.addAttribute("classuser",userService.findById(id));
        return "adminRemoveUserConfirmation";
    }

    @PostMapping("/admin/users/delete-{id}")
    public String deleteUserConfirmed(@PathVariable long id) {
        Optional<ClassUser> user = userService.findById(id);

        if(user != null){
            List<GroupClass> groupClassList = new ArrayList<>();
            List<Post> postList = new ArrayList<>();

            if(!groupClassList.isEmpty()){
                for (GroupClass groupClass : groupClassList) {
                    long groupClassId = groupClass.getClassid();
                    userService.removeGroupClass(groupClassId, user.get().getUserid());
                }
            }

            if(!postList.isEmpty()) {
                for (Post post : postList) {
                    long postId = post.getPostid();
                    userService.removePost(postId, user.get().getUserid());
                }
            }
        }

        userService.delete(id);

        return "redirect:/admin/users";
    }
}
