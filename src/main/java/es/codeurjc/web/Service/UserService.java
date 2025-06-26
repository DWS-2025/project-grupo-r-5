package es.codeurjc.web.Service;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.ClassUserMapper;
import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private PostService postService;

    @Autowired
    private ClassUserMapper classUserMapper;

    @Autowired
    private GroupClassRepository groupClassRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /*public Page<ClassUserBasicDTO> findAll(Pageable page) {
        Page<ClassUser> users = userRepository.findAll(page);
        return classUserMapper.toDTOs(users.getContent());
    }*/

    //This method will be changed on third phase
    public ClassUserDTO getLoggedUser() {    String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ClassUser user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return classUserMapper.toDTO(user);}

    public Page<ClassUserBasicDTO> findAll(Pageable page) {
        Page<ClassUser> users = userRepository.findAll(page);
        List<ClassUserBasicDTO> dtoList = users
                .getContent()
                .stream()
                .map(classUserMapper::toBasicDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, page, users.getTotalElements());
    }
    public Page<ClassUserDTO> findAllFullUser(Pageable page) {
        Page<ClassUser> users = userRepository.findAll(page);
        List<ClassUserDTO> dtoList = users
                .getContent()
                .stream()
                .map(classUserMapper::toDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, page, users.getTotalElements());
    }


    public Optional<ClassUserDTO> findById(long id) {
        return userRepository.findById(id)
                .map(classUserMapper::toDTO);
    }

    public Optional<ClassUser> findEntityById(long id) {
        return userRepository.findById(id);
    }

    public Optional<ClassUserDTO> findByName(String name) {
        return userRepository.findByUsernameIgnoreCase(name)
                .map(classUserMapper::toDTO);
    }
    public Optional<ClassUser> findEntityByName(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public ClassUserDTO save(ClassUserDTO classUserDTO){
        ClassUser classUser = classUserMapper.toDomain(classUserDTO);
        ClassUser saved = userRepository.save(classUser);
        return classUserMapper.toDTO(saved);
    }

    public ClassUserDTO save(ClassUserBasicDTO classUserBasicDTO){
        ClassUser classUser = classUserMapper.toDomain(classUserBasicDTO);
        ClassUser saved = userRepository.save(classUser);
        return classUserMapper.toDTO(saved);
    }
    public ClassUserDTO save(ClassUser classUser){
        ClassUser newUser = new ClassUser(classUser.getUsername(),passwordEncoder.encode(classUser.getPassword()),List.of("USER"));
        ClassUser saved = userRepository.save(newUser);
        return classUserMapper.toDTO(saved);
    }

    public ClassUserDTO delete(long id) {
        ClassUser classUser = userRepository.findById(id).orElseThrow();
        userRepository.deleteById(id);
        return toDTO(classUser);
    }
    public void deleteUserClasses(long id) {
        ClassUser user = userRepository.findById(id).orElseThrow();

        for (GroupClass groupClass : user.getListOfClasses()) {
            groupClass.removeUser(user);
        }

        user.getListOfClasses().clear();

    }
    public boolean usernameExists(String username) {
        Optional <ClassUser> op =  userRepository.findByUsernameIgnoreCase(username);
        return userRepository.findByUsernameIgnoreCase(username).isPresent();
    }


    @Transactional
    public Optional<ClassUserDTO> addGroupClass(long classId, long userId) {
        Optional<ClassUser> op_classUser = userRepository.findById(userId);
        Optional<GroupClass> op_groupClass = groupClassRepository.findById(classId);

        if (op_classUser.isPresent() && op_groupClass.isPresent()) {
            ClassUser classUser = op_classUser.get();
            GroupClass groupClass = op_groupClass.get();

            classUser.addClass(groupClass);
            groupClass.addUser(classUser);

            ClassUser updated = userRepository.save(classUser);
            return Optional.of(classUserMapper.toDTO(updated));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<ClassUserDTO> removeGroupClass(long classId, long userId) {
        Optional<ClassUser> op_classUser = userRepository.findById(userId);
        Optional<GroupClass> op_groupClass = groupClassRepository.findById(classId);

        if (op_classUser.isPresent() && op_groupClass.isPresent()) {

            GroupClass groupClass = op_groupClass.get();
            ClassUser classUser = op_classUser.get();

            classUser.removeClass(groupClass);
            groupClass.removeUser(classUser);

            ClassUser updated = userRepository.save(classUser);

            return Optional.of(classUserMapper.toDTO(updated));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<ClassUserDTO> addPost(long postId, long userId) throws IOException {
        Optional<ClassUser> op_classUser = userRepository.findById(userId);
        Optional<Post> op_post = postRepository.findById(postId);

        if (op_post.isPresent() && op_classUser.isPresent()) {
            Post post = op_post.get();
            ClassUser classUser = op_classUser.get();

            classUser.addPost(post);
            post.setCreator(classUser);

            ClassUser updated = userRepository.save(classUser);
            return Optional.of(classUserMapper.toDTO(updated));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<ClassUserDTO> removePost(long postId, long userId) {
        Optional<ClassUser> op_classUser = userRepository.findById(userId);
        Optional<Post> op_post = postRepository.findById(postId);

        if (op_post.isPresent() && op_classUser.isPresent()) {
            Post post = op_post.get();
            ClassUser classUser = op_classUser.get();

            classUser.removePost(post);
            post.setCreator(null);

            ClassUser updated = userRepository.save(classUser);
            return Optional.of(classUserMapper.toDTO(updated));
        }

        return Optional.empty();
    }

    public ClassUserDTO toDTO (ClassUser classUser) {
        return classUserMapper.toDTO(classUser);
    }

    public ClassUserBasicDTO toBasicDTO (ClassUser classUser) {
        return classUserMapper.toBasicDTO(classUser);
    }

    public ClassUser toDomain (ClassUserDTO classUserDTO) {
        return classUserMapper.toDomain(classUserDTO);
    }
    public ClassUser toDomain (ClassUserBasicDTO classUserBasicDTO) {
        return classUserMapper.toDomain(classUserBasicDTO);
    }

    public Page<ClassUserBasicDTO> toDTOs(Page<ClassUser> classUsersPage) {
        List<ClassUserBasicDTO> dtoList = classUsersPage
                .getContent()
                .stream()
                .map(classUserMapper::toBasicDTO) //returns ClassUserBasicDTO
                .collect(Collectors.toList());

        //If you want to reverse the order:
        //Collections.reverse(dtoList);
        return new PageImpl<>(dtoList, classUsersPage.getPageable(), classUsersPage.getTotalElements());
    }


}
