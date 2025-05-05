package es.codeurjc.web.Controller;


import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.ClassUserMapper;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Service.UserService;
import org.springframework.ui.Model;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Service.GroupClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class GroupClassWebController {

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showGroupClasses(Model model) {
        List<Map.Entry<String, List<GroupClass>>> groupedClasses = groupClassService.getClassesGroupedByDayAndSortedByTime();
        model.addAttribute("groupedClasses", groupedClasses);
        return "index";
    }
    @GetMapping("/GroupClasses/Join-{id}")
    public String joinClass(Model model , @PathVariable long id) {
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        if (groupClass.isPresent()) {
            model.addAttribute("GroupClass", groupClass);
            return "joinClass";
        } else{
            return "index";
        }
    }
    @PostMapping("/GroupClasses/Join-{id}")
    public String joinClassProcess(Model model, @RequestParam String username, @PathVariable Long id, @PathVariable Long userid) throws IOException {

        ClassUserBasicDTO user = new ClassUserBasicDTO(userid, username);
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);

        if (groupClass.isPresent()) {
            userService.save(user);
            userService.addGroupClass(id, userid);
        }
        long classid = groupClass.get().classid();
        return "redirect:/GroupClasses/Join-" + classid + "/success";

    }
    @GetMapping("/GroupClasses/Join-{id}/success")
    public String joinClassSuccess(Model model, @PathVariable long id) {
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        model.addAttribute("GroupClass", groupClass);

        return "successJoinClass";

    }
}
