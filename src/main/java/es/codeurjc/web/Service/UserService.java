package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private PostService postService;

    private ConcurrentMap<Long, ClassUser> users = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Long> userIdsByName = new ConcurrentHashMap<>();
    //private AtomicLong nextId = new AtomicLong(1L);


    public List<ClassUser> findAll() {return userRepository.findAll();}

    public Optional<ClassUser> findById(long id) {
        return userRepository.findById(id);
    }

    public Optional<ClassUser> findByName(String name) {
        return userRepository.findByName(name);
    }

    public boolean exist(long id) {
        return userRepository.existsById(id);
    }

    public ClassUser save(ClassUser classUser){
        return userRepository.save(classUser);
    }

    public void delete(long id) {userRepository.deleteById(id);}
    @Transactional
    public boolean addGroupClass(long classId, long userId) {
        Optional <ClassUser> op_classUser = userRepository.findById(userId);
        Optional <GroupClass> op_groupClass = groupClassService.findById(classId);

        if (op_classUser.isPresent() && op_groupClass.isPresent()) {
            ClassUser classUser = op_classUser.get();
            GroupClass groupClass = op_groupClass.get();

            classUser.addClass(groupClass);
            groupClass.addUser(classUser);

            //groupClassService.save(groupClass);
            save(classUser);
            return true;

        }
        return false;
    }

    public boolean removeGroupClass(long classId, long userId) {
        Optional<ClassUser> op_classUser = userRepository.findById(userId);
        Optional<GroupClass> op_groupClass = groupClassService.findById(classId);

        if (op_classUser.isPresent() && op_groupClass.isPresent()) {

            GroupClass groupClass = op_groupClass.get();
            ClassUser classUser = op_classUser.get();

            classUser.removeClass(groupClass);
            groupClass.removeUser(classUser);

            //groupClassService.save(groupClass);
            save(classUser);

            return true;
        }
        return false;
    }
    @Transactional
    public boolean addPost(long postId, long userId) throws IOException {
        Optional <ClassUser> op_classUser = userRepository.findById(userId);
        Optional <Post> op_post = postService.findById(postId);

        if(op_post.isPresent() && op_classUser.isPresent()){

            Post post = op_post.get();
            ClassUser classUser = op_classUser.get();

            classUser.addPost(post);
            post.setCreator(classUser);

            //postService.save(post, null);
            save(classUser);

            return true;
        }
        return false;
    }

    public boolean removePost(long postId, long userId) {
        Optional <ClassUser> op_classUser = userRepository.findById(userId);
        Optional <Post> op_post = postService.findById(postId);

        if(op_post.isPresent() && op_classUser.isPresent()){

            Post post = op_post.get();
            ClassUser classUser = op_classUser.get();

            post.setCreator(null);
            classUser.removePost(post);

            post.setCreator(null);
            save(classUser);

            return true;
        }
        return false;
    }

}
