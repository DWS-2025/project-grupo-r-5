package es.codeurjc.web.Repositories;

import es.codeurjc.web.Domain.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface GroupClassRepository extends JpaRepository <GroupClass, Long> {
    @Query("SELECT gc FROM GroupClass gc WHERE gc.day = :day AND gc.instructor = :instructor")
    List<GroupClass> findClassesByDayAndInstructor(@Param("day") DayOfWeek day, @Param("instructor") String instructor);
    @Query("SELECT DISTINCT g.instructor FROM GroupClass g WHERE g.instructor IS NOT NULL")
    List<String> findDistinctByInstructors();

}
