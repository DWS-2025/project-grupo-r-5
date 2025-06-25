package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
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
import java.util.List;
import java.util.Optional;

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
    public GroupClassDTO replaceGroupClass(@PathVariable long id, @RequestBody GroupClassDTO dto) {

        Optional <GroupClassDTO> op = groupClassService.findById(id);
        if(op.isPresent()) {
            GroupClassDTO original = op.get();
            int currentCapacity = original.currentCapacity();
            List<ClassUserBasicDTO> userList = original.usersList();

            GroupClassDTO updatedDto = new GroupClassDTO(
                    id,
                    dto.classname(),
                    dto.instructor(),
                    dto.day(),
                    dto.timeInit(),
                    dto.duration(),
                    dto.timeFin(),
                    dto.maxCapacity(),
                    currentCapacity,
                    userList
            );
            return groupClassService.save(updatedDto);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public GroupClassDTO deleteGroupClass(@PathVariable long id) {
        Optional <GroupClassDTO> op = groupClassService.findById(id);
        if(op.isPresent()) {
            GroupClassDTO original = op.get();
            original.usersList().size();

            groupClassService.delete(id);
            return original;
        }
        else{
            return null;
        }
    }
}
