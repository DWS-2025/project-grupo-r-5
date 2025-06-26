package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String showGroupClasses(@ModelAttribute("filter") GroupClass filter, Model model, Pageable page) {
        //Sorted by day and hour:
        Map<String, List<GroupClassDTO>> groupedMap = groupClassService.getGroupedClassesByExample(filter, page);
        //Change Map to a list of GroupedEntry
        List<GroupedEntry> groupedClasses = groupedMap.entrySet()
                .stream()
                .map(entry -> new GroupedEntry(entry.getKey(), entry.getValue()))
                .toList();

        model.addAttribute("groupedClasses", groupedClasses);

        //model.addAttribute("groupedClasses", groupClassService.findAll(page));
        return "index";
    }

    //Dynamic Query:
    @GetMapping("/GroupClasses")
    public String listFilteredClasses(
            @RequestParam(name = "dayWeek", required = false) String dayWeek,
            @RequestParam(required = false) String instructor,
            @RequestParam(required = false) Boolean search,
            @PageableDefault(size = 5)Pageable pageable,
            Model model
    ) {

        if (search == null || !search) {
            //No search, show empty formulary
            model.addAttribute("searchPerformed", false);
            return "classesList";
        }

        instructor = Optional.ofNullable(validateService.cleanInstructor(instructor))
                .filter(cleaned -> !cleaned.isBlank())
                .orElse(null);

        DayOfWeek dayOfWeek = validateService.parseAndCleanDay(dayWeek);

        Page<GroupClassDTO> page = groupClassService.findClassesByExample(dayOfWeek, instructor, pageable);

        model.addAttribute("searchPerformed", true);
        model.addAttribute("classes", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("dayWeek", dayOfWeek != null ? dayOfWeek.name() : "Any");
        model.addAttribute("instructor", instructor != null ? instructor : "Any");
        model.addAttribute("previousPage", page.hasPrevious() ? page.getNumber() - 1 : 0);
        model.addAttribute("nextPage", page.hasNext() ? page.getNumber() + 1 : 0);


        return "classesList";
    }
    @PostMapping("/GroupClasses")
    public String findClassesPost(@RequestParam(name = "dayWeek", required = false) String dayWeek,
                                  @RequestParam(required = false) String instructor,
                                  @PageableDefault(size = 5)Pageable pageable,
                                  Model model) {

        // Clean and validate instructor
        instructor = Optional.ofNullable(validateService.cleanInstructor(instructor))
                .filter(cleaned -> !cleaned.isBlank())
                .orElse(null);

        // Parse and clean day
        DayOfWeek dayOfWeek = validateService.parseAndCleanDay(dayWeek);

        // Fetch filtered classes
        Page<GroupClassDTO> page = groupClassService.findClassesByExample(dayOfWeek, instructor, pageable);


        // Add attributes to the model
        model.addAttribute("searchPerformed", true);
        model.addAttribute("classes", page.getContent());
        model.addAttribute("currentPage", page.getNumber() + 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("dayWeek", dayOfWeek != null ? dayOfWeek.name() : "Any");
        model.addAttribute("instructor", instructor != null ? instructor : "Any");

        //For pagination:
        model.addAttribute("previousPage", page.hasPrevious() ? page.getNumber() - 1: 0);
        model.addAttribute("nextPage", page.hasNext() ? page.getNumber() + 1 : 0);

        return "classesList";
    }


    @GetMapping("/GroupClasses/Join-{id}")
    public String joinClass(Model model , @PathVariable long id) {
        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        if (groupClass.isPresent()) {
            model.addAttribute("groupClass", groupClass.get());
            return "joinClass";
        } else{
            return "index";
        }
    }

    @PostMapping("/GroupClasses/Join-{id}")
    public String joinClassProcess(Model model, @PathVariable Long id, RedirectAttributes redirectAttributes) throws IOException {

        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        Optional<GroupClassDTO> optionalGroupClass = groupClassService.findById(id);

        if (optionalGroupClass.isEmpty()) {
            redirectAttributes.addAttribute("message", "The class does not exist.");
            return "redirect:/error";
        }

        ClassUserDTO user = userService.getLoggedUser();
        if(groupClass.isPresent() && groupClass.get().usersList().contains(user)){
            redirectAttributes.addAttribute("message", "The user is already registered in this class.");
            return "redirect:/error";
        }
        /*if (groupClass.isPresent() && !groupClass.get().usersList().contains(user)) {
            GroupClassDTO groupClassDTO = groupClass.get();

            userService.addGroupClass(id,user.userid());
        }*/
        userService.addGroupClass(id,user.userid());
        //long classid = groupClass.get().classid();
        return "redirect:/GroupClasses/Join-" + id + "/success";

    }

    @GetMapping("/GroupClasses/Join-{id}/success")
    public String joinClassSuccess(Model model, @PathVariable long id, RedirectAttributes redirectAttributes) {

        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);

        if(groupClass.isPresent()) {
            model.addAttribute("GroupClass", groupClass.get());
        } else {
            redirectAttributes.addAttribute("message", "The class does not exist.");
            return "redirect:/error";
        }
        return "successJoinClass";

    }




}



