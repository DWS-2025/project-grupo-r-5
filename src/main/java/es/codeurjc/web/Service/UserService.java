package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.repository.ClassUserRepository;
import es.codeurjc.web.repository.GroupClassRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    @Autowired
    private ClassUserRepository classUserRepository;

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private GroupClassRepository groupClassRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private EntityManager entityManager;

    private AtomicLong nextId = new AtomicLong(1L);
    private Long classUserId;


    public List<ClassUser> findAll(){return classUserRepository.findAll();}

    public Optional<ClassUser> findById(long id){return classUserRepository.findById(id);}

    public ClassUser save(ClassUser classUser){
        long id = nextId.getAndIncrement();
        classUser.setUserid(id);
        classUserRepository.save(classUser);
        return classUser;
    }


    public Optional<ClassUser> findByName(String user) {
        return classUserRepository.findByName(user);
    }

    @Transactional
    public void addPost(Post post, Long userid) {
        ClassUser classUser = classUserRepository.findById(userid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userid));

        if (!postService.exist(post.getPostid())) {
            postService.save(post);
        } else {
            Post finalPost = post;
            post = postService.findById(post.getPostid()).orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + finalPost.getPostid()));
        }

        classUser.getListOfPosts().add(post);
        classUserRepository.save(classUser); //Error in image and showing post
    }
}
