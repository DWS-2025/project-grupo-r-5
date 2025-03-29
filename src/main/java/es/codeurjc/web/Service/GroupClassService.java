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



    public List<GroupClass> findAll() {return groupClassRepository.findAll();}

    public Optional<GroupClass> findById(long id) {return groupClassRepository.findById(id);}

    public void save(GroupClass groupClass) {
        groupClassRepository.save(groupClass);
    }

    public void delete(long id) {
        groupClassRepository.deleteById(id);
    }

    //-----------------------------------------------------------------------------
    //Change with DataBase usage
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
    //-----------------------------------------------------------------------------

}
