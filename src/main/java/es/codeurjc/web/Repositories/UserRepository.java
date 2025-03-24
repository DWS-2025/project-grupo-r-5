package es.codeurjc.web.Repositories;

import es.codeurjc.web.Model.ClassUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository <ClassUser, Long>{

}
