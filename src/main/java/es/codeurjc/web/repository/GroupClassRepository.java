package es.codeurjc.web.repository;


import es.codeurjc.web.Model.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupClassRepository extends JpaRepository<GroupClass, Long> { }
