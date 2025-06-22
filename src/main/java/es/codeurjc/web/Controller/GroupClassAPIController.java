package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.GroupClassBasicDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Service.GroupClassService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/groupClasses")
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
    public GroupClassDTO getGroupClass(@PathVariable Long id) {
        return groupClassService.findById(id).orElseThrow();
    }

    @PostMapping("/")
    public GroupClassDTO createGroupClass(@RequestBody GroupClassDTO groupClassDTO) {
        groupClassDTO = groupClassService.save(groupClassDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(groupClassDTO.classid())
                .toUri();
        return ResponseEntity.created(location).body(groupClassDTO).getBody();
    }

    @PutMapping("/{id}")
    public GroupClassDTO replaceGroupClass(@PathVariable long id, @RequestBody GroupClassDTO updatedGroupClassDTO) {
        return groupClassService.save(updatedGroupClassDTO);
    }

    @DeleteMapping("/{id}")
    public GroupClassDTO deleteGroupClass(@PathVariable long id) {
        return groupClassService.delete(id);
    }
}
