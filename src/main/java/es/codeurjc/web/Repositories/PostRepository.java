package es.codeurjc.web.Repositories;

import es.codeurjc.web.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository <Post, Long> {

}
