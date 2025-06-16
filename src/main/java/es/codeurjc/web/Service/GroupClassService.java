package es.codeurjc.web.Service;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Dto.ClassUserBasicDTO;
import es.codeurjc.web.Dto.GroupClassBasicDTO;
import es.codeurjc.web.Dto.GroupClassDTO;
import es.codeurjc.web.Dto.GroupClassMapper;
import es.codeurjc.web.Repositories.GroupClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class GroupClassService {
    @Autowired
    private GroupClassRepository groupClassRepository;

    @Autowired
    private GroupClassMapper groupClassMapper;

    //private ConcurrentHashMap<Long, GroupClass> groupClasses = new ConcurrentHashMap<>();

    public GroupClassService() {}


    /*public Page<GroupClassBasicDTO> findAll(Pageable page) {
        Page<GroupClass> groupClasses = groupClassRepository.findAll(page);
        return groupClassMapper.toDTOs(groupClasses.getContent());
    }*/

    public Page<GroupClassBasicDTO> findAll(Pageable page) {
        Page<GroupClass> groupClasses = groupClassRepository.findAll(page);
        List<GroupClassBasicDTO> dtoList = groupClasses
                .getContent()
                .stream()
                .map(groupClassMapper::toBasicDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, page, groupClasses.getTotalElements());
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

    public GroupClassDTO save(GroupClassDTO groupClassDTO) {
        GroupClass groupClass = groupClassMapper.toDomain(groupClassDTO);
        GroupClass saved = groupClassRepository.save(groupClass);
        return groupClassMapper.toDTO(saved);
    }

    public GroupClassDTO delete(long id) {
        GroupClass groupClass = groupClassRepository.findById(id).orElseThrow();
        groupClassRepository.deleteById(id);
        return toDTO(groupClass);
    }

    public Map<String, List<GroupClassDTO>> getGroupedClassesByExample(GroupClass filter, Pageable page) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues().withIgnorePaths("duration", "maxCapacity", "currentCapacity", "usersList");
        Example<GroupClass> example = Example.of(filter, matcher);

        //Add manual order (day and hour)
        Pageable sortedPage = PageRequest.of(
                page.getPageNumber(),
                page.getPageSize(),
                Sort.by("day").ascending().and(Sort.by("timeInit").ascending())
        );

        Page<GroupClass> result = groupClassRepository.findAll(example, sortedPage);
        List<GroupClassDTO> dtoList = result
                .stream()
                .map(groupClassMapper::toDTO)
                .toList();

        //Group by day
        return dtoList.stream()
                .collect(Collectors.groupingBy(
                        GroupClassDTO::day,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    //Actual dynamic query with QBE and Spring Data JPA
    public Page<GroupClassDTO> findClassesByExample(DayOfWeek day, String instructor, Pageable pageable) {
        GroupClass exampleClass = new GroupClass();
        if (day != null) exampleClass.setDay(day);
        if (instructor != null && !instructor.isBlank()) exampleClass.setInstructor(instructor);

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        Example<GroupClass> example = Example.of(exampleClass, matcher);

        Page<GroupClass> page = groupClassRepository.findAll(example, pageable);

        return page.map(groupClassMapper::toDTO);
    }


    public GroupClassDTO toDTO(GroupClass groupClass) {
        return groupClassMapper.toDTO(groupClass);
    }

    public GroupClassBasicDTO toBasicDTO(GroupClass groupClass) {
        return groupClassMapper.toBasicDTO(groupClass);
    }

    public GroupClass toDomain(GroupClassBasicDTO groupClassDTO) {
        return groupClassMapper.toDomain(groupClassDTO);
    }

    /*
    public Page<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses) {
        return groupClassMapper.toDTOs(groupClasses);
    } */

    public Page<GroupClassBasicDTO> toDTOs(Page<GroupClass> groupClassPage) {
        List<GroupClassBasicDTO> dtoList = groupClassPage
                .getContent()
                .stream()
                .map(groupClassMapper::toBasicDTO) //returns ClassUserBasicDTO
                .collect(Collectors.toList());

        //If you want to reverse the order:
        //Collections.reverse(dtoList);
        return new PageImpl<>(dtoList, groupClassPage.getPageable(), groupClassPage.getTotalElements());
    }

}

