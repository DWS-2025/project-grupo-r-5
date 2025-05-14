package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Service.GroupClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class GroupClassWebController {

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateService validateService;

    @GetMapping("/")
    public String showGroupClasses(@ModelAttribute("filter") GroupClass filter, Model model, Pageable page) {
        //Sorted by day and hour:
        Map<String, List<GroupClassDTO>> groupedClasses = groupClassService.getGroupedClassesByExample(filter, page);
        model.addAttribute("groupedClasses", groupedClasses);

        //model.addAttribute("groupedClasses", groupClassService.findAll(page));
        return "index";
    }

    //Dynamic Query:
    @GetMapping("/GroupClasses")
    public String listFilteredClasses(
            @RequestParam(required = false) DayOfWeek day,
            @RequestParam(required = false) String instructor,
            Pageable pageable,
            Model model
    ) {
        Page<GroupClassDTO> classes = groupClassService.findClassesByExample(day, instructor, pageable);
        model.addAttribute("classes", classes);
        return "classesList";
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

    @PostMapping("/GroupClasses")
    public String findClassesPost(@RequestParam(required = false) String day,
                                  @RequestParam(required = false) String instructor,
                                  Pageable pageable,
                                  Model model) {

        String cleanedInstructor = validateService.cleanInstructor(instructor);
        if(cleanedInstructor != null && !cleanedInstructor.isBlank()) {
            cleanedInstructor = instructor;
        } else {
            cleanedInstructor = null;
        }

        String cleanedDay = validateService.cleanDay(DayOfWeek.valueOf(day));
        if (cleanedDay != null && !cleanedDay.isBlank()) {
            day = cleanedDay;
        } else {
            day = null;
        }

        Page<GroupClassDTO> page = groupClassService.findClassesByExample(DayOfWeek.valueOf(day), instructor, pageable);
        model.addAttribute("classes", page.getContent());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("dayWeek", day);
        model.addAttribute("instructor", instructor);

        return "classesList";
    }

}
