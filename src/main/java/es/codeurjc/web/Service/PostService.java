package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.Post;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import es.codeurjc.web.Repositories.PostRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static es.codeurjc.web.Service.ImageService.IMAGES_FOLDER;

@Service
public class PostService {
    @Autowired
    private ImageService imageService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    private ConcurrentMap<Long,Post> posts = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1L);

    public PostService(){}

    @PostConstruct
    public void init() {
        // Now the users are available to be saved
        ClassUser classUser = new ClassUser("Manolo");
        ClassUser classUser2 = new ClassUser("Julian");
        ClassUser classUser3 = new ClassUser("Rufusberto");

        // Saves users using UserService
        userService.save(classUser);
        userService.save(classUser2);
        userService.save(classUser3);

        try {
            save(new Post(classUser, "Pedaleando al ritmo de la música", "¡Menuda clase de spinning! La música y la energía del instructor hicieron que la hora pasara volando. Nos hizo subir la resistencia poco a poco hasta acabar con un sprint final que me dejó sin aliento. Ahora me duelen las piernas, pero la sensación de haberlo dado todo no tiene precio. ¡Repetiré seguro! #SpinningVibes #CardioTotal"),null);
            save(new Post(classUser2, "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga y salí como nuevo. La instructora nos guió con mucha paciencia y nos ayudó a mejorar la postura en cada asana. Al principio me costó concentrarme, pero al final sentí una paz increíble. ¡Definitivamente volveré la próxima semana! #YogaTime #Relajación #Bienestar"), null);
            save(new Post(classUser3, "Nunca había sudado tanto", "¡La clase de CrossFit de hoy fue brutal! Sentía que no iba a poder con tantas repeticiones, pero la motivación del grupo me empujó a seguir. Hicimos sentadillas con peso, burpees y sprints... ¡Acabé agotado pero feliz! Si alguien busca un reto de verdad, esta clase es la mejor opción. #CrossFitLovers #NoPainNoGain"), null);
            save(new Post(classUser2, "Bailar y entrenar al mismo tiempo", "Hoy fue mi primera clase de zumba y ¡me encantó! Al principio me sentí un poco perdido con los pasos, pero el ambiente era tan divertido que pronto me solté. Una hora bailando sin parar y sin darme cuenta de que estaba haciendo ejercicio. ¡Recomendado 100%! #ZumbaLovers #EjercicioDivertido"), null);
            save(new Post(classUser, "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa. Saltos, movimientos rápidos y mucha coordinación… ¡pero qué bien se siente después! La música te motiva a seguir y el instructor hace que no quieras rendirte. Definitivamente es una de las mejores maneras de quemar calorías sin aburrirse. #AerobicsPower #EnergíaPura"), null);
            save(new Post(classUser2, "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates y me sorprendió lo exigente que puede ser. Pensé que sería solo estiramientos, pero trabajamos fuerza, control y respiración de una forma increíble. Me encantó cómo al final todo el cuerpo se siente más ligero y fuerte al mismo tiempo. ¡Definitivamente seguiré viniendo! #PilatesLovers #FuerzaYFlexibilidad"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        userService.addPost(1, classUser.getUserid());
        userService.addPost(2, classUser2.getUserid());
        userService.addPost(3, classUser3.getUserid());
        userService.addPost(4, classUser2.getUserid());
        userService.addPost(5, classUser.getUserid());
        userService.addPost(6, classUser2.getUserid());

    }

    //Methods
    public Collection<Post> findAll(){return posts.values();}

    public Optional<Post>findById(long id){
        if(this.posts.containsKey(id)){
            return Optional.of(this.posts.get(id));
        }
        return Optional.empty();
    }

    public boolean exist(long id){return posts.containsKey(id);}

    public void save(Post post, MultipartFile imageFile) throws IOException {
        // If imageFile isn't null, we use ImageService to save it
        if (imageFile != null && !imageFile.isEmpty()) {
            String path = imageService.createImage(imageFile);
            post.setImagePath(path);
        }

        if(post.getImagePath() == null || post.getImagePath().isEmpty()) post.setImagePath("no-image.png");


        long id = nextId.getAndIncrement();
        post.setPostid(id);
        posts.put(id, post);
    }

    public void delete(long id){
        posts.remove(id);
    }

    public void edit(Post post, MultipartFile imageFile, long id) throws IOException {
        Post newP = posts.get(id);
        if(newP == null){ //If the post is not valid
            System.out.println("Post not found\n");
        } else {
            newP.setTitle(post.getTitle());
            newP.setDescription(post.getDescription());
            newP.setCreator(post.getCreator());

            if (imageFile != null && !imageFile.isEmpty()) {
                String path = imageService.createImage(imageFile);
                post.setImagePath(path);
            }

            if(post.getImagePath() == null || post.getImagePath().isEmpty()) post.setImagePath("no-image.png");

            //We don't need to actualize the id
            posts.put(id, newP);
            userService.addPost(newP.getPostid(), newP.getCreator().getUserid());
        }
    }

    public boolean removeUser(long userId, long postId){
        Post post = posts.get(postId);
        ClassUser classUser = userService.findById(userId);
        if(post != null && classUser != null){
            return post.removeUser(classUser);
        }
        return false;
    }

}
