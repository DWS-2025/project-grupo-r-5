package es.codeurjc.web.Repositories;

import es.codeurjc.web.Model.ClassUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository <ClassUser, Long>{
    Optional<ClassUser> findByName(String name);

    /* Another way:
    @Query("SELECT u FROM ClassUser u WHERE u.name = :name")
    List<ClassUser> getClassUserByName (@Param("name") String name);
    */
}
