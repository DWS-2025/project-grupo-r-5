package es.codeurjc.web.Controller;

import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

@Controller
public class DataBaseUsage implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private GroupClassRepository groupClassRepository;

    //Initializes the data
    @Override
    public void run(String... args) throws Exception {
        //slide 7 in presentation 2.2 BBDD
    }
}
