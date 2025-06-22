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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    public record GroupedEntry(String key, List<GroupClassDTO> value) {}

    @GetMapping("/")
    public String showGroupClasses(@ModelAttribute("filter") GroupClass filter, Model model, Pageable page, RedirectAttributes redirectAttributes) {
        try {
            //Sorted by day and hour:
            Map<String, List<GroupClassDTO>> groupedMap = groupClassService.getGroupedClassesByExample(filter, page);

            // Check if the result is null or empty
            if (groupedMap == null || groupedMap.isEmpty()) {
                redirectAttributes.addAttribute("message", "No group classes found.");
                return "redirect:/error";
            }

            //Change the Map to a list of objects GroupedEntry
            List<GroupedEntry> groupedClasses = groupedMap.entrySet()
                    .stream()
                    .map(entry -> new GroupedEntry(entry.getKey(), entry.getValue()))
                    .toList();

            model.addAttribute("groupedClasses", groupedClasses);

            //model.addAttribute("groupedClasses", groupClassService.findAll(page));
            return "index";
        } catch (Exception e) {
            // Handle the exception, log it, or return an error view
            redirectAttributes.addAttribute("message", "An error occurred while fetching group classes.");
            return "redirect:/error";
        }
    }

    //Dynamic Query:
    @GetMapping("/GroupClasses")
    public String listFilteredClasses(
            @RequestParam(required = false) DayOfWeek day,
            @RequestParam(required = false) String instructor,
            Pageable pageable,
            Model model
    ) {
        try {
            Page<GroupClassDTO> classes = groupClassService.findClassesByExample(day, instructor, pageable);
            model.addAttribute("classes", classes);
            return "classesList";
        }
        catch (Exception e) {
            // Handle the exception, log it, or return an error view
            model.addAttribute("message", "An error occurred while fetching classes.");
            return "redirect:/error";
        }
    }


    @GetMapping("/GroupClasses/Join-{id}")
    public String joinClass(Model model , @PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
            if (groupClass.isPresent()) {
                model.addAttribute("groupClass", groupClass.get());
                return "joinClass";
            } else {
                redirectAttributes.addAttribute("message", "Group class not found.");
                return "redirect:/error";
                //return "index";
            }
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", "An error occurred while fetching the class.");
            return "redirect:/error";
        }
    }

    @GetMapping("/GroupClasses/Join-{id}/success")
    public String joinClassSuccess(Model model, @PathVariable long id, RedirectAttributes redirectAttributes) {
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        model.addAttribute("GroupClass", groupClass);
        if(groupClass.isPresent()){
            return "successJoinClass";
        } else {
            redirectAttributes.addAttribute("message", "Group class not found.");
            return "redirect:/error";
        }
    }


    @PostMapping("/GroupClasses/Join-{id}")
    public String joinClassProcess(Model model, @RequestParam String username, @PathVariable Long id, @PathVariable Long userid, RedirectAttributes redirectAttributes) throws IOException {

        ClassUserBasicDTO user = new ClassUserBasicDTO(userid, username);
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);

        if (groupClass.isPresent()) {
            userService.save(user);
            userService.addGroupClass(id, userid);
        } else {
            redirectAttributes.addAttribute("message", "Group class not found.");
            return "redirect:/error";
        }
        long classid = groupClass.get().classid();
        return "redirect:/GroupClasses/Join-" + classid + "/success";

    }

    @PostMapping("/GroupClasses")
    public String findClassesPost(@RequestParam(required = false) String day,
                                  @RequestParam(required = false) String instructor,
                                  Pageable pageable,
                                  Model model, RedirectAttributes redirectAttributes) {

        try {
            String cleanedInstructor = validateService.cleanInstructor(instructor);
            if (cleanedInstructor != null && !cleanedInstructor.isBlank()) {
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

        } catch (Exception e){
            redirectAttributes.addAttribute("message", "An error occurred while fetching classes.");
            return "redirect:/error";
        }
    }

}



