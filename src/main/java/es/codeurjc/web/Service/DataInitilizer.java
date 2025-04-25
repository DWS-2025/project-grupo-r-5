package es.codeurjc.web.Service;


import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.ClassUserMapper;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Transactional
@Service
public class DataInitilizer {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private PostService postService;

    @Autowired
    private ClassUserMapper classUserMapper;

    /*
    public void init() throws IOException {
        // Crear los posts
        Post post1 = new Post(classUser4, "Pedaleando al ritmo de la música", "¡Menuda clase de spinning!...");
        Post post2 = new Post(classUser5, "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga...");
        Post post3 = new Post(classUser6, "Nunca había sudado tanto", "¡La clase de CrossFit de hoy fue brutal!...");
        Post post4 = new Post(classUser5, "Bailar y entrenar al mismo tiempo", "Hoy fue mi primera clase de zumba y ¡me encantó!");
        Post post5 = new Post(classUser4, "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa.");
        Post post6 = new Post(classUser5, "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates y me sorprendió lo exigente que puede ser.");

        // Guardar los posts de una en una
        userService.addPost(post1.getPostid(), classUser1.getUserid());
        userService.addPost(post2.getPostid(), classUser2.getUserid());
        userService.addPost(post3.getPostid(), classUser3.getUserid());
        userService.addPost(post4.getPostid(), classUser4.getUserid());
        userService.addPost(post5.getPostid(), classUser5.getUserid());
        userService.addPost(post6.getPostid(), classUser6.getUserid());
    }*/

    @PostConstruct
    public void init() throws IOException {
        initializeGroupClasses();
        initializeUsers();

        //To assign users to classes
        List<GroupClass> groupClasses = groupClassService.findAll();
        List<ClassUser> users = userService.findAll().stream()
                .map(classUserMapper::toDomain) //Convert DTOs back to domain objects
                .toList();
        assignUsersToClasses(groupClasses, users);

        initializePosts();
    }

    private void initializeGroupClasses() {
        GroupClass[] groupClasses = {
                new GroupClass("Advanced yoga", DayOfWeek.MONDAY, LocalTime.parse("10:00"), 60, "Professor A", 20),
                new GroupClass("Pilates", DayOfWeek.TUESDAY, LocalTime.parse("15:00"), 120, "Professor B", 15),
                new GroupClass("CrossFit", DayOfWeek.WEDNESDAY, LocalTime.parse("18:00"), 45, "Professor C", 25),
                new GroupClass("Zumba", DayOfWeek.THURSDAY, LocalTime.parse("12:00"), 60, "Professor D", 30),
                new GroupClass("Spinning", DayOfWeek.FRIDAY, LocalTime.parse("17:00"), 60, "Professor E", 20),
                new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("09:00"), 60, "Professor F", 25),
                new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("10:00"), 60, "Professor F", 25)
        };

        for (GroupClass groupClass : groupClasses) {
            groupClassService.save(groupClass);
        }
    }

    private void initializeUsers() {
        ClassUser[] users = {
                new ClassUser("Pepe"),
                new ClassUser("Juan"),
                new ClassUser("Maria"),
                new ClassUser("Manolo"),
                new ClassUser("Julian"),
                new ClassUser("Rufusberto")
        };

        for (ClassUser user : users) {
            userService.save(classUserMapper.toDTO(user));
        }

    }
    private void assignUsersToClasses(List<GroupClass> groupClasses, List<ClassUser> users) {
        // Example: Assign the first two users to the first group class
        if (!groupClasses.isEmpty() && users.size() >= 2) {
            //ClassUser1 and 2 in the first group class (yoga)
            userService.addGroupClass(groupClasses.get(0).getClassid(), users.get(0).getUserid());
            userService.addGroupClass(groupClasses.get(0).getClassid(), users.get(1).getUserid());
            //ClassUser1 in the second group class (pilates)
            userService.addGroupClass(groupClasses.get(1).getClassid(), users.get(0).getUserid());
            //ClassUser3 in the third group class (crossfit)
            userService.addGroupClass(groupClasses.get(2).getClassid(), users.get(2).getUserid());
            //ClassUser1, 2 and 3 in the fourth group class (zumba)
            userService.addGroupClass(groupClasses.get(3).getClassid(), users.get(0).getUserid());
            userService.addGroupClass(groupClasses.get(3).getClassid(), users.get(1).getUserid());
            userService.addGroupClass(groupClasses.get(3).getClassid(), users.get(2).getUserid());
            //ClassUser1 in the fifth group class (spinning)
            userService.addGroupClass(groupClasses.get(4).getClassid(), users.get(0).getUserid());
            //ClassUser3 in the sixth group class (aerobics)
            userService.addGroupClass(groupClasses.get(5).getClassid(), users.get(2).getUserid());
        }
    }

    private void initializePosts() {
        Post[] posts = {
                new Post(new ClassUser("Manolo"), "Pedaleando al ritmo de la música", "¡Menuda clase de spinning!..."),
                new Post(new ClassUser("Julian"), "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga..."),
                new Post(new ClassUser("Rufusberto"), "Nunca había sudado tanto", "¡La clase de CrossFit de hoy fue brutal!..."),
                new Post(new ClassUser("Julian"), "Bailar y entrenar al mismo tiempo", "Hoy fue mi primera clase de zumba y ¡me encantó!"),
                new Post(new ClassUser("Manolo"), "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa."),
                new Post(new ClassUser("Julian"), "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates y me sorprendió lo exigente que puede ser.")
        };


        try {
            assignPostsToUsers(posts);
        } catch (IOException e) {
            System.err.println("Error assigning posts to users: " + e.getMessage());
        }

    }

    public void assignPostsToUsers(Post[] posts) throws IOException {
        for (int i = 0; i < posts.length; i++) {
            Post post = posts[i];
            List<ClassUserBasicDTO> classUsers = userService.findAll();
            ClassUserBasicDTO user = classUsers.get(i % classUsers.size()); // Cycle through users if posts > users
            userService.addPost(post.getPostid(), classUserMapper.toDomain(user).getUserid());
        }
    }

}
