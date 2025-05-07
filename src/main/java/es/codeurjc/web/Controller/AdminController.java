package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.ClassUserMapper;
import es.codeurjc.web.Dto.GroupClassBasicDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
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
    public String showPosts(Model model, Pageable page) {
        model.addAttribute("posts", postService.findAll(page));
        return "adminPosts";
    }

    //Page it:
    @GetMapping("/admin/users")
    public String showUsers(Model model, Pageable page) {
        model.addAttribute("users", classUserService.findAll(page));
        return "adminUsers";
    }

    //Page it:
    @GetMapping("/admin/groupClasses")
    public String showGroupClasses(Model model, Pageable page) {
        model.addAttribute("GroupClasses", groupClassService.findAll(page));
        return "adminGroupClasses";
    }

    @GetMapping("/admin/groupClasses/delete-{id}")
    public String deleteGroupClass(@PathVariable long id, Model model) {
        model.addAttribute("groupClass",groupClassService.findById(id));
        return "adminRemoveClassConfirmation";
    }

    @PostMapping("/admin/groupClasses/delete-{id}")
    public String deleteGroupClassConfirmed(@PathVariable long id) {
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);

        if (groupClass.isPresent()) {
            List<ClassUserDTO> usersList = new ArrayList<>(groupClass.get().usersList());

            if (!usersList.isEmpty()) {
                for (ClassUserDTO user : usersList) {
                    long userId = user.userid();
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
        GroupClassBasicDTO newgpClassBasicDTO = groupClassService.toBasicDTO(newClass);
        groupClassService.save(newgpClassBasicDTO);

        return "redirect:/admin/groupClasses";  // Redirect to a success page
    }

    @GetMapping("/admin/users/delete-{id}")
    public String deleteUser(@PathVariable long id, Model model) {
        model.addAttribute("classuser",userService.findById(id));
        return "adminRemoveUserConfirmation";
    }

    @PostMapping("/admin/users/delete-{id}")
    public String deleteUserConfirmed(@PathVariable long id) {
        Optional<ClassUserDTO> user = userService.findById(id);

        if(user.isPresent()){
            List<GroupClass> groupClassList = new ArrayList<>();
            List<Post> postList = new ArrayList<>();

            if(!groupClassList.isEmpty()){
                for (GroupClass groupClass : groupClassList) {
                    long groupClassId = groupClass.getClassid();
                    userService.removeGroupClass(groupClassId, user.get().userid());
                }
            }

            if(!postList.isEmpty()) {
                for (Post post : postList) {
                    long postId = post.getPostid();
                    userService.removePost(postId, user.get().userid());
                }
            }
        }

        userService.delete(id);

        return "redirect:/admin/users";
    }
}
