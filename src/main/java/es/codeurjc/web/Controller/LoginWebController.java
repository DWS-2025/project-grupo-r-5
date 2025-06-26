package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import es.codeurjc.web.security.SecurityConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateService validateService;

    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        return "login";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }

    @GetMapping("/newUser")
    public String showCreateForm(Model model) {
        model.addAttribute("userForm", new ClassUser());
        model.addAttribute("usernameError", false);
        return "usercreateform";
    }

    @PostMapping("/newUser")
    public String newUserProcess(Model model,
                                 @RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 RedirectAttributes redirectAttributes) {

        //Create DTO or entity
        username = username.trim();
        ClassUser newUser = new ClassUser();
        newUser.setUsername(validateService.cleanInput(username));
        newUser.setPassword(validateService.cleanInput(password));

        //Validate existing username
        if (userService.usernameExists(username)) {
            model.addAttribute("error", "Ese nombre ya está en uso");
            model.addAttribute("userForm", newUser);
            return "usercreateform";
        }

        //Validate user (you can do extra validations)
        String validationError = validateService.validateUsername(username);
        if (validationError != null && !validationError.isEmpty()) {
            model.addAttribute("error", validationError);
            model.addAttribute("userForm", newUser);
            return "usercreateform"; // formulario con error
        }

        //Save user
        userService.save(newUser);

        return "redirect:/profile";
    }
}
