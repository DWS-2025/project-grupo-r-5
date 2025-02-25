package es.codeurjc.web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {
    @GetMapping("/error")
    public String handleError(@RequestParam(name = "message", required = false, defaultValue = "An unexpected error occurred.") String message,
                              Model model) {
        model.addAttribute("errorMessage", message);
        return "errorPage";
    }
}
