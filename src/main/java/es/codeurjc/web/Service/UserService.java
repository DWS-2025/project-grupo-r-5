package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.repository.ClassUserRepository;
import es.codeurjc.web.repository.GroupClassRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    private EntityManager entityManager;

    private AtomicLong nextId = new AtomicLong(1L);
    private Long classUserId;


    public List<ClassUser> findAll(){return classUserRepository.findAll();}

    public ClassUser save(ClassUser classUser){
        long id = nextId.getAndIncrement();
        classUser.setUserid(id);
        classUserRepository.save(classUser);
        return classUser;
    }


}
