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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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


    public List<ClassUserBasicDTO> findAll() {
        List<ClassUser> users = userRepository.findAll();
        return classUserMapper.toDTOs(users);}

    public Optional<ClassUserDTO> findById(long id) {
        return userRepository.findById(id)
                .map(classUserMapper::toDTO);
    }

    public Optional<ClassUserDTO> findByName(String name) {
        return userRepository.findByName(name)
                .map(classUserMapper::toDTO);
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

    public void delete(long id) {userRepository.deleteById(id);}

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

}
