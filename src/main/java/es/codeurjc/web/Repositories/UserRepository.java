package es.codeurjc.web.Repositories;

import es.codeurjc.web.Domain.ClassUser;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <ClassUser, Long>{
    Optional<ClassUser> findByUsername(String username);

    /* Another way:
    @Query("SELECT u FROM ClassUser u WHERE u.name = :name")
    List<ClassUser> getClassUserByName (@Param("name") String name);
    */
}
