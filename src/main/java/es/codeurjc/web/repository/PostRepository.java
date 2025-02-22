package es.codeurjc.web.repository;


import es.codeurjc.web.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;
import java.util.UUID;


@Validated
public interface PostRepository extends JpaRepository<Post, UUID> { }
