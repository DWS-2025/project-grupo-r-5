package es.codeurjc.web.Controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        int statusCode = (status != null) ? Integer.parseInt(status.toString()) : 500;
        String errorMessage="An unexpected error happened";

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            errorMessage = "La página que buscas no existe.";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            errorMessage = "No tienes permiso para acceder a esta página.";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            errorMessage = "Se produjo un error en el servidor.";
        }

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        return "errorPage";
    }
}
