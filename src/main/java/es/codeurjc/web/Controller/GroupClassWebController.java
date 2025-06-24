package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.ClassUserDTO;
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

    public record GroupedEntry(String key, List<GroupClassDTO> value) {}

    @GetMapping("/")
    public String showGroupClasses(@ModelAttribute("filter") GroupClass filter, Model model, Pageable page) {
        //Sorted by day and hour:
        Map<String, List<GroupClassDTO>> groupedMap = groupClassService.getGroupedClassesByExample(filter, page);
        // Convertimos el Map en una lista de objetos GroupedEntry
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
            Pageable pageable,
            Model model
    ) {
        DayOfWeek dayOfWeek = parseAndCleanDay(dayWeek);
        Page<GroupClassDTO> classes = groupClassService.findClassesByExample(dayOfWeek, instructor, pageable);
        model.addAttribute("classes", classes);
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
    public String joinClassProcess(Model model, @PathVariable Long id) throws IOException {

        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        ClassUserDTO user = userService.getLoggedUser();
        if (groupClass.isPresent() && !groupClass.get().usersList().contains(user)) {
            GroupClassDTO groupClassDTO=  groupClass.get();

            userService.addGroupClass(id,user.userid());
        }
        long classid = groupClass.get().classid();
        return "redirect:/GroupClasses/Join-" + classid + "/successJoinClass";

    }

    @GetMapping("/GroupClasses/Join-{id}/success")
    public String joinClassSuccess(Model model, @PathVariable long id) {

        Optional<GroupClassDTO> groupClass = groupClassService.findById(id);
        model.addAttribute("GroupClass", groupClass.get());

        return "successJoinClass";

    }


    private DayOfWeek parseAndCleanDay(String day) {
        if (day == null || day.isBlank() || day.equalsIgnoreCase("Any")) {
            return null;
        }
        try {
            DayOfWeek rawDay = DayOfWeek.valueOf(day.toUpperCase());
            String cleanedDay = validateService.cleanDay(rawDay);
            return validateService.parseDay(cleanedDay);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid day provided: " + day);
            return null; // Invalid day, return null
        }
    }

    @PostMapping("/GroupClasses")
    public String findClassesPost(@RequestParam(name = "dayWeek", required = false) String dayWeek,
                                  @RequestParam(required = false) String instructor,
                                  Pageable pageable,
                                  Model model) {

        System.out.println("findClassesPost called with day=" + dayWeek + ", instructor=" + instructor + ", page=" + pageable.getPageNumber());

        // Clean and validate instructor
        instructor = Optional.ofNullable(validateService.cleanInstructor(instructor))
                .filter(cleaned -> !cleaned.isBlank())
                .orElse(null);

        // Parse and clean day
        DayOfWeek dayOfWeek = parseAndCleanDay(dayWeek);

        // Fetch filtered classes
        Page<GroupClassDTO> page = groupClassService.findClassesByExample(dayOfWeek, instructor, pageable);

        // Add attributes to the model
        model.addAttribute("searchPerformed", true);
        model.addAttribute("classes", page.getContent());
        model.addAttribute("currentPage", page.getNumber());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("dayWeek", dayOfWeek != null ? dayOfWeek.name() : "Any");
        model.addAttribute("instructor", instructor != null ? instructor : "Any");

        //For pagination:
        model.addAttribute("previousPage", page.hasPrevious() ? page.getNumber() - 1 : null);
        model.addAttribute("nextPage", page.hasNext() ? page.getNumber() + 1 : null);

        return "classesList";
    }

}



