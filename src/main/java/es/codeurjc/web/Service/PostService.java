package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.Post;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static es.codeurjc.web.Service.ImageService.IMAGES_FOLDER;

@Service
public class PostService {
    @Autowired
    public ImageService imageService;
    @Autowired
    public UserService userService;

    private ConcurrentMap<Long,Post> posts = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1L);

    public PostService(){}

    @PostConstruct
    public void init() {
        // Ahora los usuarios están disponibles para ser guardados
        ClassUser classUser = new ClassUser("Manolo");
        ClassUser classUser2 = new ClassUser("Julian");
        ClassUser classUser3 = new ClassUser("Rufusberto");

        // Guarda los usuarios usando el userService
        userService.save(classUser);
        userService.save(classUser2);
        userService.save(classUser3);

        try {
            save(new Post(classUser, "Pedaleando al ritmo de la música", "¡Menuda clase de spinning! ..."), null);
            save(new Post(classUser2, "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga y ..."), null);
            save(new Post(classUser3, "Nunca había sudado tanto", "¡La clase de CrossFit de hoy fue brutal! ..."), null);
            save(new Post(classUser2, "Bailar y entrenar al mismo tiempo", "Hoy fue mi primera clase de zumba ..."), null);
            save(new Post(classUser, "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa ..."), null);
            save(new Post(classUser2, "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates ..."), null);
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

    public Post findById(long id){return posts.get(id);}

    public boolean exist(long id){return posts.containsKey(id);}

    public void save(Post post, MultipartFile imageFile) throws IOException {
        // Si el archivo de imagen no es null, usamos el ImageService para guardarlo
        if (imageFile != null && !imageFile.isEmpty()) {
            // Usamos el servicio de imágenes para guardar la imagen y obtener el nombre del archivo
            String imageName = imageService.createImage(imageFile);
            post.setImageName(imageName);
            post.setImagePath(imageService.getImage(imageName).getURI().toString()); // Puedes guardar la URL de la imagen
        }

        // Asignamos un nuevo ID al post
        long postId = nextId.getAndIncrement();
        post.setPostid(postId);

        // Guardamos el post en el mapa concurrente
        posts.put(postId, post);
    }

    public void delete(long id){
        posts.remove(id);
    }

}
