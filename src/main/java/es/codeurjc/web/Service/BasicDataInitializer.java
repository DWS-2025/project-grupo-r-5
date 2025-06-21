package es.codeurjc.web.Service;


import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Repositories.GroupClassRepository;
import es.codeurjc.web.Repositories.PostRepository;
import es.codeurjc.web.Repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Transactional
@Service
public class BasicDataInitializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupClassRepository groupClassRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageService imageService;


    @PostConstruct
    public void init() throws IOException {
        //create users first:
        ClassUser user1 = new ClassUser("Pepe");
        ClassUser user2 = new ClassUser("Juan");
        ClassUser user3 = new ClassUser("Yoshi");
        ClassUser user4 = new ClassUser("Zelda");
        ClassUser user5 = new ClassUser("Rufusberto");
        ClassUser user6 = new ClassUser("Kirby");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);


        //group classes next:
        GroupClass class1 = new GroupClass("Advanced yoga", DayOfWeek.MONDAY, LocalTime.parse("10:00"), 60, "Professor A", 20);
        GroupClass class2 = new GroupClass("Pilates", DayOfWeek.TUESDAY, LocalTime.parse("15:00"), 120, "Professor B", 15);
        GroupClass class3 = new GroupClass("Zumba", DayOfWeek.WEDNESDAY, LocalTime.parse("12:00"), 90, "Professor C", 25);
        GroupClass class4 = new GroupClass("CrossFit", DayOfWeek.THURSDAY, LocalTime.parse("18:00"), 45, "Professor D", 30);
        GroupClass class5 = new GroupClass("Spinning", DayOfWeek.FRIDAY, LocalTime.parse("17:00"), 60, "Professor E", 20);
        GroupClass class6 = new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("09:00"), 60, "Professor F", 25);
        GroupClass class7 = new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("10:00"), 60, "Professor F", 25);

        groupClassRepository.save(class1);
        groupClassRepository.save(class2);
        groupClassRepository.save(class3);
        groupClassRepository.save(class4);
        groupClassRepository.save(class5);
        groupClassRepository.save(class6);
        groupClassRepository.save(class7);

        //Assign users to group classes:
        userService.addGroupClass(class1.getClassid(), user1.getUserid());
        userService.addGroupClass(class1.getClassid(), user2.getUserid());
        userService.addGroupClass(class2.getClassid(), user3.getUserid());
        userService.addGroupClass(class2.getClassid(), user4.getUserid());
        userService.addGroupClass(class3.getClassid(), user5.getUserid());
        userService.addGroupClass(class3.getClassid(), user6.getUserid());
        userService.addGroupClass(class3.getClassid(), user1.getUserid());
        userService.addGroupClass(class4.getClassid(), user2.getUserid());
        userService.addGroupClass(class5.getClassid(), user3.getUserid());
        userService.addGroupClass(class5.getClassid(), user4.getUserid());
        userService.addGroupClass(class6.getClassid(), user5.getUserid());



        //Recover users to initialize posts:
        ClassUser managedUser1 = userRepository.findById(user1.getUserid()).orElseThrow();
        ClassUser managedUser5 = userRepository.findById(user5.getUserid()).orElseThrow();
        ClassUser managedUser6 = userRepository.findById(user6.getUserid()).orElseThrow();


        //Then create posts:
        Post post1 = new Post(managedUser1, "Pedaleando al ritmo de la música", "¡Menuda clase de spinning!...");
        Post post2 = new Post(managedUser5, "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga...");
        Post post3 = new Post(managedUser6, "Nunca había sudado tanto", "¡La clase de CrossFit de hoy fue brutal!...");
        Post post4 = new Post(managedUser5, "Bailar y entrenar al mismo tiempo", "Hoy fue mi primera clase de zumba y ¡me encantó!");
        Post post5 = new Post(managedUser1, "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa.");
        Post post6 = new Post(managedUser5, "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates y me sorprendió lo exigente que puede ser.");

        //set images:
        post1.setImagePath("example1.jpeg");
        post2.setImagePath("example2.jpeg");
        post3.setImagePath("example3.jpeg");
        post4.setImagePath("example4.jpeg");

        Blob image1 = imageService.imageFileFromPath(post1.getImagePath());
        Blob image2 = imageService.imageFileFromPath(post2.getImagePath());
        Blob image3 = imageService.imageFileFromPath(post3.getImagePath());
        Blob image4 = imageService.imageFileFromPath(post4.getImagePath());

        post1.setImageFile(image1);
        post2.setImageFile(image2);
        post3.setImageFile(image3);
        post4.setImageFile(image4);

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);

        //Assign users to posts:
        userService.addPost(post1.getPostid(), user1.getUserid());
        userService.addPost(post2.getPostid(), user5.getUserid());
        userService.addPost(post3.getPostid(), user6.getUserid());
        userService.addPost(post4.getPostid(), user5.getUserid());
        userService.addPost(post5.getPostid(), user1.getUserid());
        userService.addPost(post6.getPostid(), user5.getUserid());

    }

}
