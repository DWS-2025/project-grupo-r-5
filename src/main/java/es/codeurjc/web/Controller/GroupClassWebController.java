package es.codeurjc.web.Controller;


import ch.qos.logback.core.model.Model;
import es.codeurjc.web.Service.GroupClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GroupClassWebController {

    @Autowired
    private GroupClassService groupClassService;

    @GetMapping("/")
    public String showGroupClasses(Model model) {


        return "index";
    }


}
