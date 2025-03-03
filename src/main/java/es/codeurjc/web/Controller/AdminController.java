package es.codeurjc.web.Controller;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import org.springframework.ui.Model;
import es.codeurjc.web.Service.GroupClassService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {
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
    public String showPosts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "adminPosts";
    }

    @GetMapping("/admin/users")
    public String showUsers(Model model) {
        model.addAttribute("users", classUserService.findAll());
        return "adminUsers";
    }

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
        GroupClass groupClass = groupClassService.findById(id);
        List<ClassUser> usersList= groupClass.getUsersList();
        if(!usersList.isEmpty()){
            for (ClassUser user:usersList){
                long userId = user.getUserid();
                userService.removeGroupClass(id,userId);
                groupClassService.removeUser(id,userId);
            }
        }
        groupClassService.delete(id);
        return "redirect:/admin/groupClasses";
    }

}
