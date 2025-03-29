package es.codeurjc.web.Service;


import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class DataInitilizer {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupClassService groupClassService;

    @Autowired
    private GroupClassRepository groupClassRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @PostConstruct
    public void init() throws IOException {
        // Guardar clases en la base de datos
        GroupClass yoga = groupClassRepository.save(new GroupClass("Advanced yoga", DayOfWeek.MONDAY, LocalTime.parse("10:00"), 60, "Professor A", 20));
        GroupClass pilates = groupClassRepository.save(new GroupClass("Pilates", DayOfWeek.TUESDAY, LocalTime.parse("15:00"), 120, "Professor B", 15));
        GroupClass crossfit = groupClassRepository.save(new GroupClass("CrossFit", DayOfWeek.WEDNESDAY, LocalTime.parse("18:00"), 45, "Professor C", 25));
        GroupClass zumba = groupClassRepository.save(new GroupClass("Zumba", DayOfWeek.THURSDAY, LocalTime.parse("12:00"), 60, "Professor D", 30));
        GroupClass spinning = groupClassRepository.save(new GroupClass("Spinning", DayOfWeek.FRIDAY, LocalTime.parse("17:00"), 60, "Professor E", 20));
        GroupClass aerobics1 = groupClassRepository.save(new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("09:00"), 60, "Professor F", 25));
        GroupClass aerobics2 = groupClassRepository.save(new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("10:00"), 60, "Professor F", 25));

        // Guardar usuarios en la base de datos
        ClassUser classUser1 = userRepository.save(new ClassUser("Pepe"));
        ClassUser classUser2 = userRepository.save(new ClassUser("Juan"));
        ClassUser classUser3 = userRepository.save(new ClassUser("Maria"));
        ClassUser classUser4 = userRepository.save(new ClassUser("Manolo"));
        ClassUser classUser5 = userRepository.save(new ClassUser("Julian"));
        ClassUser classUser6 = userRepository.save(new ClassUser("Rufusberto"));

        // Agregar usuarios a clases
        userService.addGroupClass(yoga.getClassid(), classUser1.getUserid());
        userService.addGroupClass(yoga.getClassid(), classUser2.getUserid());
        userService.addGroupClass(pilates.getClassid(), classUser1.getUserid());
        userService.addGroupClass(crossfit.getClassid(), classUser3.getUserid());
        userService.addGroupClass(zumba.getClassid(), classUser1.getUserid());
        userService.addGroupClass(zumba.getClassid(), classUser2.getUserid());
        userService.addGroupClass(zumba.getClassid(), classUser3.getUserid());
        userService.addGroupClass(spinning.getClassid(), classUser1.getUserid());
        userService.addGroupClass(aerobics1.getClassid(), classUser3.getUserid());

        // Guardar posts con usuarios correctos
        Post post1 = postRepository.save(new Post(classUser4, "Pedaleando al ritmo de la música", "¡Menuda clase de spinning!..."));
        Post post2 = postRepository.save(new Post(classUser5, "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga..."));
        Post post3 = postRepository.save(new Post(classUser6, "Nunca había sudado tanto", "¡La clase de CrossFit de hoy fue brutal!..."));
        Post post4 = postRepository.save(new Post(classUser5, "Bailar y entrenar al mismo tiempo", "Hoy fue mi primera clase de zumba y ¡me encantó!"));
        Post post5 = postRepository.save(new Post(classUser4, "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa."));
        Post post6 = postRepository.save(new Post(classUser5, "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates y me sorprendió lo exigente que puede ser."));

        // Asociar posts con usuarios
        userService.addPost(post1.getPostid(), classUser4.getUserid());
        userService.addPost(post2.getPostid(), classUser5.getUserid());
        userService.addPost(post3.getPostid(), classUser6.getUserid());
        userService.addPost(post4.getPostid(), classUser5.getUserid());
        userService.addPost(post5.getPostid(), classUser4.getUserid());
        userService.addPost(post6.getPostid(), classUser5.getUserid());
    }


}
