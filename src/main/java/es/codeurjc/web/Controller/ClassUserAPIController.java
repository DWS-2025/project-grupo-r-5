package es.codeurjc.web.Controller;

import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Service.GroupClassService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/classUser")
public class ClassUserAPIController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public Page<ClassUserBasicDTO> getClassUsers(Pageable page) {
        return userService.findAll(page);
    }

    @GetMapping("/{id}")
    public ClassUserDTO getClassUser(@PathVariable long id) {
        return userService.findById(id).orElseThrow();
    }

    @PostMapping("/")
    public ClassUserDTO createClassUser(ClassUserDTO classUserDTO) {
        classUserDTO = userService.save(classUserDTO);
        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(classUserDTO.userid()).toUri();
        return ResponseEntity.created(location).body(classUserDTO).getBody();
    }

    @PutMapping("/{id}")
    public ClassUserDTO replaceClassUser(@PathVariable long id, @RequestBody ClassUserDTO updatedClassUserDTO) {
        return userService.save(updatedClassUserDTO);
    }

    @DeleteMapping("/{id}")
    public ClassUserDTO deleteClassUser(@PathVariable long id) {
        return userService.delete(id);
    }

}
