package es.codeurjc.web.Service;

import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Dto.GroupClassBasicDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Dto.GroupClassMapper;
import es.codeurjc.web.Repositories.GroupClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GroupClassService {
    @Autowired
    private GroupClassRepository groupClassRepository;

    @Autowired
    private GroupClassMapper groupClassMapper;

    //private ConcurrentHashMap<Long, GroupClass> groupClasses = new ConcurrentHashMap<>();

    public GroupClassService() {}

    public Page<GroupClassBasicDTO> findAll(Pageable page) {
        Page<GroupClass> groupClasses = groupClassRepository.findAll(page);
        return groupClassMapper.toDTOs(groupClasses.getContent());
    }

    public Optional<GroupClassDTO> findById(long id) {
        return groupClassRepository.findById(id)
                .map(groupClassMapper::toDTO);
    }

    public GroupClassDTO save(GroupClassBasicDTO groupClassDTO) {
        GroupClass groupClass = groupClassMapper.toDomain(groupClassDTO);
        GroupClass saved = groupClassRepository.save(groupClass);
        return groupClassMapper.toDTO(saved);
    }

    public void delete(long id) {
        groupClassRepository.deleteById(id);
    }


    //-----------------------------------------------------------------------------
    //Change with DataBase usage
    /*
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
    } */

    public GroupClassDTO toDTO(GroupClass groupClass) {
        return groupClassMapper.toDTO(groupClass);
    }

    public GroupClassBasicDTO toBasicDTO(GroupClass groupClass) {
        return groupClassMapper.toBasicDTO(groupClass);
    }

    public GroupClass toDomain(GroupClassBasicDTO groupClassDTO) {
        return groupClassMapper.toDomain(groupClassDTO);
    }
    public Page<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses) {
        return groupClassMapper.toDTOs(groupClasses);
    }

}
