package es.codeurjc.web.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClassUserWebController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String userProfile(Model model, HttpServletRequest request) {
        ClassUserDTO loggedUser = userService.getLoggedUser();
        model.addAttribute("user", loggedUser);

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        return "profile";  // perfil.mustache
    }

}
