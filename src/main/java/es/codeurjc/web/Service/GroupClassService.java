package es.codeurjc.web.Service;


import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Repositories.GroupClassRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GroupClassService {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupClassRepository groupClassRepository;

    private ConcurrentHashMap<Long, GroupClass> groupClasses = new ConcurrentHashMap<>();
    private AtomicLong nextId = new AtomicLong(1L);



    public GroupClassService() {}

    @PostConstruct
    public void init() {
        save(new GroupClass("Advanced yoga", DayOfWeek.MONDAY, LocalTime.parse("10:00"), 60, "Professor A", 20));
        save(new GroupClass("Pilates", DayOfWeek.TUESDAY, LocalTime.parse("15:00"), 120, "Professor B", 15));
        save(new GroupClass("CrossFit", DayOfWeek.WEDNESDAY, LocalTime.parse("18:00"), 45, "Professor C", 25));
        save(new GroupClass("Zumba", DayOfWeek.THURSDAY, LocalTime.parse("12:00"), 60, "Professor D", 30));
        save(new GroupClass("Spinning", DayOfWeek.FRIDAY, LocalTime.parse("17:00"), 60, "Professor E", 20));
        save(new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("09:00"), 60, "Professor F", 25));
        save(new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("10:00"), 60, "Professor F", 25));

        userService.addGroupClass(1,1);
        userService.addGroupClass(1,2);
        userService.addGroupClass(2,1);
        userService.addGroupClass(3,3);
        userService.addGroupClass(4,1);
        userService.addGroupClass(4,2);
        userService.addGroupClass(4,3);
        userService.addGroupClass(5,1);
        userService.addGroupClass(6,3);

        addUser(1, 1);  // Add user 1 to class 1
        addUser(1, 2);  // Add user 2 to class 1
        addUser(2, 1);  // Add user 1 to class 2
        addUser(3, 3);  // Add user 3 to class 3
        addUser(4, 1);  // Etc.
        addUser(4, 2);
        addUser(4, 3);
        addUser(5, 1);
        addUser(6, 3);
    }


    public Collection<GroupClass> findAll() {return groupClasses.values();}

    public GroupClass findById(long id) {
        return groupClasses.get(id);
    }
    //public Optional<GroupClass> findById(long id) {return groupClassRepository.findById(id);}

    public void save(GroupClass groupClass) {
        long id = nextId.getAndIncrement();
        groupClass.setClassid(id);
        this.groupClasses.put(id, groupClass);
    }

    public void delete(long id) {
        this.groupClasses.remove(id);
    }

    //Change later
    public boolean addUser(long groupId, long userId) {
        GroupClass groupClass = this.groupClasses.get(groupId);
        /*ClassUser classUser = userService.findById(userId);
        if (groupClass != null) {
            return groupClass.addUser(classUser);
        }*/
        return false;
    }

    //Change later
    public boolean removeUser(long groupId, long userId) {
        GroupClass groupClass = this.groupClasses.get(groupId);
        /*ClassUser classUser = userService.findById(userId);
        if (groupClass != null && classUser != null) {
            return groupClass.removeUser(classUser);
        }*/
        return false;
    }

    public List<Map.Entry<String, List<GroupClass>>> getClassesGroupedByDayAndSortedByTime() {
        List<GroupClass> allClasses = new ArrayList<>(groupClasses.values());

        // Order by day of the week and then by hour
        allClasses.sort(Comparator
                .comparing(GroupClass::getDay)
                .thenComparing(GroupClass::getTime_init));

        // Group in a map with String as key
        Map<String, List<GroupClass>> groupedClasses = new LinkedHashMap<>();

        for (GroupClass groupClass : allClasses) {
            String dayAsString = groupClass.getDay().toString();
            groupedClasses
                    .computeIfAbsent(dayAsString, k -> new ArrayList<>())
                    .add(groupClass);
        }

        // Convert the map to an entry list
        return new ArrayList<>(groupedClasses.entrySet());
    }

}
