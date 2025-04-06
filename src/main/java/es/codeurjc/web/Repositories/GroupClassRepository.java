package es.codeurjc.web.Repositories;

import es.codeurjc.web.Model.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.List;

public interface GroupClassRepository extends JpaRepository <GroupClass, Long> {
    @Query("SELECT g FROM GroupClass g WHERE g.day = :day")
    List<GroupClass> getGroupClassByDay(@Param("day") DayOfWeek day);

    @Query("SELECT g FROM GroupClass g WHERE g.instructor = :instructor")
    List<GroupClass> getGroupClassByInstructor(@Param("instructor") String instructor);

    @Query("SELECT gc FROM GroupClass gc WHERE gc.day = :day AND gc.instructor = :instructor")
    List<GroupClass> findClassesByDayAndInstructor(@Param("day") DayOfWeek day, @Param("instructor") String instructor);


}
