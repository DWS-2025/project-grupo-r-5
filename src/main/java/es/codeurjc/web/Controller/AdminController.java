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
        model.addAttribute("posts", postService.findAll());
        model.addAttribute("users", classUserService.findAll());
        model.addAttribute("classes", groupClassService.findAll());
        return "admin";
    }
}
