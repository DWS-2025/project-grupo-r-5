package es.codeurjc.web.Controller;


import org.springframework.ui.Model;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Service.GroupClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class GroupClassWebController {

    @Autowired
    private GroupClassService groupClassService;

    @GetMapping("/")
    public String showGroupClasses(Model model) {
        List<Map.Entry<String, List<GroupClass>>> groupedClasses = groupClassService.getClassesGroupedByDayAndSortedByTime();
        model.addAttribute("groupedClasses", groupedClasses);
        return "index";
    }




}
