package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
    @Lazy
    private GroupClassService groupClassService;

    @Autowired
    @Lazy
    private PostService postService;

    private ConcurrentMap<Long, ClassUser> users = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Long> userIdsByName = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1L);

    public UserService() {
        save(new ClassUser("Pepe"));
        save(new ClassUser("Juan"));
        save(new ClassUser("Maria"));
    }

    public Collection<ClassUser> findAll() {
        return users.values();
    }

    public ClassUser findById(long id) {
        return users.get(id);
    }

    public ClassUser findByName(String name) {
        Long userId = userIdsByName.get(name);
        return (userId != null) ? users.get(userId) : null;
    }

    public boolean exist(long id) {
        return users.containsKey(id);
    }

    public void save(ClassUser classUser){
        long id = nextId.getAndIncrement();
        classUser.setUserid(id);
        this.users.put(id, classUser);
    }

    public void delete(long id) {users.remove(id);}

    public boolean addGroupClass(long classId, long userId) {
        ClassUser classUser = users.get(userId);
        GroupClass groupClass = groupClassService.findById(classId);
        if (classUser != null) {
            return classUser.addClass(groupClass);
        }
        return false;
    }

    public boolean removeGroupClass(long classId, long userId) {
        ClassUser classUser = users.get(classId);
        GroupClass groupClass = groupClassService.findById(classId);
        if (classUser != null && groupClass != null) {
            return classUser.removeClass(groupClass);
        }
        return false;
    }

    public boolean addPost(long postId, long userId) {
        ClassUser classUser = this.users.get(userId);
        Optional <Post> op = postService.findById(postId);
        if(op.isPresent() && classUser != null){
            Post post = op.get();
            return classUser.addPost(post);
        }

        return false;
    }

    public boolean removePost(long postId, long userId) {
        ClassUser classUser = this.users.get(userId);
        Optional <Post> op = postService.findById(postId);
        if(op.isPresent() && classUser != null){
            Post post = op.get();
            return classUser.removePost(post);
        }
        return false;
    }

}
