package es.codeurjc.web.Controller;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.Post;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.ClassUserDTO;
import es.codeurjc.web.Dto.PostDTO;
import es.codeurjc.web.Service.ImageService;
import es.codeurjc.web.Service.PostService;
import es.codeurjc.web.Service.UserService;
import es.codeurjc.web.Service.ValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/posts")
public class BlogAPIController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ValidateService validateService;


    //CRUD operations for Post:
    @GetMapping("/")
    public Page<PostDTO> getPosts(Pageable page) {
        return postService.findAll(page);
    }

    @GetMapping("/{id}")
    public PostDTO getPost(@PathVariable long id) {
        return postService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

    }

    @PostMapping("/")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ClassUserDTO> userOpt = userService.findByName(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }
        ClassUserDTO loggedUser = userOpt.get();

        // Asignar creador al post
        Post post = postService.toDomain(postDTO);
        post.setCreator(userService.toDomain(loggedUser));

        // Validar el post
        String validationError = validateService.validatePost(post);
        if (validationError != null && !validationError.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationError);
        }

        // Guardar post
        PostDTO savedPostDTO = postService.save(postService.toDTO(post));

        // Devolver id o la URL para que el cliente lo use
        return ResponseEntity
                .created(URI.create("/api/posts/" + savedPostDTO.postid()))
                .body(savedPostDTO);
    }

    @PutMapping("/{id}")
    public void editPost(@PathVariable long id, @RequestBody PostDTO postDTO) throws IOException {
        PostDTO originalPost = postService.getPost(id);
        if (originalPost == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El post no existe");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ClassUserDTO> userOpt = userService.findByName(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }
        ClassUserDTO loggedUser = userOpt.get();

        if (!isOwnerOrAdmin(id, loggedUser.username())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para editar este post");
        }

        Post updated = postService.toDomain(postDTO);

        updated.setPostid(id);
        updated.setCreator(userService.toDomain(originalPost.creator()));
        updated.setImagePath(originalPost.imagePath());

        String validationError = validateService.validatePost(updated);
        if (validationError != null && !validationError.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationError);
        }

        postService.save(postService.toDTO(updated));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id) {
        PostDTO postDTO = postService.getPost(id);
        if (postDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El post no existe");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ClassUserDTO> userOpt = userService.findByName(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }
        ClassUserDTO loggedUser = userOpt.get();

        if (!isOwnerOrAdmin(id, loggedUser.username())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este post");
        }

        // Borrar la imagen asociada si existe
        if (postDTO.imagePath() != null && !postDTO.imagePath().isBlank() && !"no-image.png".equals(postDTO.imagePath())) {
            imageService.deleteImage(postDTO.imagePath());
        }

        postService.delete(id);

        return ResponseEntity.noContent().build();
    }


    //CRUD operations for Images:
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> getPostImage(@PathVariable long id) throws SQLException, IOException {
        Blob postImage = postService.getBlobImage(id);
        if (postImage == null) {
            Map<String, String> error = Map.of("error", "Image not found for post with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        byte[] imageBytes = postImage.getBytes(1, (int) postImage.length());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(imageBytes);
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPostImage(@PathVariable long id,
                                                  @RequestParam("imagefile") MultipartFile imagefile) throws IOException {
        PostDTO postDTO = postService.getPost(id);
        if (postDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El post no existe");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<ClassUserDTO> userOpt = userService.findByName(username);
        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado");
        }
        ClassUserDTO loggedUser = userOpt.get();

        if (!isOwnerOrAdmin(id, loggedUser.username())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para modificar la imagen de este post");
        }

        if (imagefile == null || imagefile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha proporcionado una imagen válida");
        }

        // Crear Post temporal para validación
        Post postDomain = postService.toDomain(postDTO);
        ClassUser creatorDomain = postDomain.getCreator();
        Post postForValidation;
        try {
            postForValidation = new Post(
                    creatorDomain,
                    postDomain.getTitle(),
                    postDomain.getDescription(),
                    imagefile.getOriginalFilename(),
                    null
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear el objeto Post para validación", e);
        }

        String imageValidationError = validateService.validatePostWithImage(postForValidation, imagefile);
        if (imageValidationError != null && !imageValidationError.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, imageValidationError);
        }

        // Guardar imagen usando imageService (genera nombre único y valida extensión)
        String imageName = imageService.createImage(imagefile);

        // Borrar imagen anterior si no es la genérica
        if (postDTO.imagePath() != null && !postDTO.imagePath().isBlank() && !"no-image.png".equals(postDTO.imagePath())) {
            imageService.deleteImage(postDTO.imagePath());
        }

        // Actualizar PostDTO con nueva imagen
        PostDTO updatedPost = new PostDTO(
                postDTO.postid(),
                postDTO.creator(),
                postDTO.title(),
                postDTO.description(),
                imageName
        );

        postService.save(updatedPost);

        return ResponseEntity.ok("Imagen subida correctamente");
    }

    // PUT: solo admin o creador
    @PutMapping("/{id}/image")
    public ResponseEntity<Object> replacePostImage(@PathVariable long id, @RequestParam MultipartFile imageFile)
            throws IOException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!isOwnerOrAdmin(id, username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este post");
        }

        postService.updateImageForPost(id, imageFile);
        return ResponseEntity.noContent().build();
    }

    // DELETE: solo admin o creador
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deletePostImage(@PathVariable long id) throws IOException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!isOwnerOrAdmin(id, username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para eliminar este post");
        }

        postService.deleteImageByPostId(id);
        return ResponseEntity.noContent().build();
    }


    private boolean isOwnerOrAdmin(long postId, String username) {
        Optional<PostDTO> op = postService.findById(postId);

        if (op.isEmpty()) return false;

        Post post = postService.toDomain(op.get());
        boolean isCreator = post.getCreator().getUsername().equals(username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isCreator || isAdmin;
    }


}
