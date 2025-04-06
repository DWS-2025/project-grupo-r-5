package es.codeurjc.web.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model, @RequestParam(value = "message", required = false) String message) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;
        String errorMessage = (message != null) ? message : "An unexpected error happened";

        if (statusCode == HttpStatus.NOT_FOUND.value() && message == null) {
            errorMessage = "La página que buscas no existe";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() && message == null) {
            errorMessage = "Se produjo un error en el servidor";
        } else if (message != null) {
            errorMessage = message;
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        return "errorPage";
    }
}
