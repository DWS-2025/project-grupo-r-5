package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;


@Component
public class DatabaseInitializer {

    @Autowired
    private GroupClassService groupClass;

    @Autowired
    private UserService users;

    @PostConstruct
    public void init()throws IOException {

        ClassUser user1 = new ClassUser("Juan");
        ClassUser user2 = new ClassUser("María");
        ClassUser user3 = new ClassUser("Pedro");
        users.save(user1);
        users.save(user2);
        users.save(user3);

        GroupClass class1 = new GroupClass("Advanced yoga", DayOfWeek.MONDAY, LocalTime.parse("10:00"),60, "Professor A", 20, true);
        GroupClass class2 = new GroupClass("Pilates", DayOfWeek.TUESDAY, LocalTime.parse("15:00"),120, "Professor B", 15, true);
        GroupClass class3 = new GroupClass("CrossFit", DayOfWeek.WEDNESDAY, LocalTime.parse("18:00"),45, "Professor C", 25, true);
        GroupClass class4 = new GroupClass("Zumba", DayOfWeek.THURSDAY, LocalTime.parse("12:00"),60, "Professor D", 30, true);
        GroupClass class5 = new GroupClass("Spinning", DayOfWeek.FRIDAY, LocalTime.parse("17:00"),60, "Professor E", 20, true);
        GroupClass class6 = new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("09:00"),60, "Professor F", 25, true);
        GroupClass class7 = new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("10:00"),60, "Professor F", 25, true);


        groupClass.save(class1);
        groupClass.save(class2);
        groupClass.save(class3);
        groupClass.save(class4);
        groupClass.save(class5);
        groupClass.save(class6);
        groupClass.save(class7);


    }

}
