package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T20:32:31+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class GroupClassMapperImpl implements GroupClassMapper {

    @Autowired
    private ClassUserMapper classUserMapper;

    @Override
    public GroupClassDTO toDTO(GroupClass groupClass) {
        if ( groupClass == null ) {
            return null;
        }

        Long classid = null;
        String classname = null;
        String instructor = null;
        String day = null;
        LocalTime timeInit = null;
        int duration = 0;
        int maxCapacity = 0;
        int currentCapacity = 0;
        List<ClassUserBasicDTO> usersList = null;

        classid = groupClass.getClassid();
        classname = groupClass.getClassname();
        instructor = groupClass.getInstructor();
        if ( groupClass.getDay() != null ) {
            day = groupClass.getDay().name();
        }
        timeInit = groupClass.getTimeInit();
        duration = groupClass.getDuration();
        maxCapacity = groupClass.getMaxCapacity();
        currentCapacity = groupClass.getCurrentCapacity();
        usersList = classUserMapper.toDTOs( groupClass.getUsersList() );

        LocalTime timeFin = groupClass.getTimeFin();

        GroupClassDTO groupClassDTO = new GroupClassDTO( classid, classname, instructor, day, timeInit, duration, timeFin, maxCapacity, currentCapacity, usersList );

        return groupClassDTO;
    }

    @Override
    public GroupClassBasicDTO toBasicDTO(GroupClass groupClass) {
        if ( groupClass == null ) {
            return null;
        }

        Long classid = null;
        String classname = null;
        String instructor = null;
        String day = null;
        LocalTime timeInit = null;
        int duration = 0;
        LocalTime timeFin = null;
        int maxCapacity = 0;
        int currentCapacity = 0;

        classid = groupClass.getClassid();
        classname = groupClass.getClassname();
        instructor = groupClass.getInstructor();
        if ( groupClass.getDay() != null ) {
            day = groupClass.getDay().name();
        }
        timeInit = groupClass.getTimeInit();
        duration = groupClass.getDuration();
        timeFin = groupClass.getTimeFin();
        maxCapacity = groupClass.getMaxCapacity();
        currentCapacity = groupClass.getCurrentCapacity();

        GroupClassBasicDTO groupClassBasicDTO = new GroupClassBasicDTO( classid, classname, instructor, day, timeInit, duration, timeFin, maxCapacity, currentCapacity );

        return groupClassBasicDTO;
    }

    @Override
    public List<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses) {
        if ( groupClasses == null ) {
            return null;
        }

        List<GroupClassBasicDTO> list = new ArrayList<GroupClassBasicDTO>( groupClasses.size() );
        for ( GroupClass groupClass : groupClasses ) {
            list.add( toBasicDTO( groupClass ) );
        }

        return list;
    }

    @Override
    public GroupClass toDomain(GroupClassBasicDTO groupClassDTO) {
        if ( groupClassDTO == null ) {
            return null;
        }

        GroupClass groupClass = new GroupClass();

        groupClass.setClassid( groupClassDTO.classid() );
        groupClass.setClassname( groupClassDTO.classname() );
        groupClass.setInstructor( groupClassDTO.instructor() );
        if ( groupClassDTO.day() != null ) {
            groupClass.setDay( Enum.valueOf( DayOfWeek.class, groupClassDTO.day() ) );
        }
        groupClass.setTimeInit( groupClassDTO.timeInit() );
        groupClass.setDuration( groupClassDTO.duration() );
        groupClass.setMaxCapacity( groupClassDTO.maxCapacity() );
        groupClass.setCurrentCapacity( groupClassDTO.currentCapacity() );

        return groupClass;
    }

    @Override
    public GroupClass toDomain(GroupClassDTO groupClassDTO) {
        if ( groupClassDTO == null ) {
            return null;
        }

        GroupClass groupClass = new GroupClass();

        groupClass.setClassid( groupClassDTO.classid() );
        groupClass.setClassname( groupClassDTO.classname() );
        groupClass.setInstructor( groupClassDTO.instructor() );
        if ( groupClassDTO.day() != null ) {
            groupClass.setDay( Enum.valueOf( DayOfWeek.class, groupClassDTO.day() ) );
        }
        groupClass.setTimeInit( groupClassDTO.timeInit() );
        groupClass.setDuration( groupClassDTO.duration() );
        groupClass.setMaxCapacity( groupClassDTO.maxCapacity() );
        groupClass.setCurrentCapacity( groupClassDTO.currentCapacity() );
        groupClass.setUsersList( classUserBasicDTOListToClassUserList( groupClassDTO.usersList() ) );

        return groupClass;
    }

    protected List<ClassUser> classUserBasicDTOListToClassUserList(List<ClassUserBasicDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<ClassUser> list1 = new ArrayList<ClassUser>( list.size() );
        for ( ClassUserBasicDTO classUserBasicDTO : list ) {
            list1.add( classUserMapper.toDomain( classUserBasicDTO ) );
        }

        return list1;
    }
}
