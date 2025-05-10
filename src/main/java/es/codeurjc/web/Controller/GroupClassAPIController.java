package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.GroupClassBasicDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Service.GroupClassService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groupClass")
public class GroupClassAPIController {
    @Autowired
    private GroupClassService groupClassService;
    @Autowired
    private UserService userService;
    @Autowired
    private ValidateService validateService;


    @GetMapping("/")
    public Page<GroupClassBasicDTO> getGroupClasses(Pageable page) {
        return groupClassService.findAll(page);
    }

    @GetMapping("/{id}")
    public GroupClassDTO getGroupClass(long id) {
        return groupClassService.findById(id).orElseThrow();
    }

}
