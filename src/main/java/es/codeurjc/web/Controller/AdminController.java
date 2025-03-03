package es.codeurjc.web.Controller;

import org.springframework.ui.Model;
import es.codeurjc.web.Service.GroupClassService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
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

}
