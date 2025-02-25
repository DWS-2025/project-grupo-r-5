package es.codeurjc.web.repository;

import es.codeurjc.web.Model.ClassUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassUserRepository extends JpaRepository<ClassUser, Long> {
    Optional<ClassUser> findByName(String user);
}
