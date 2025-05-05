package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T19:05:21+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class GroupClassMapperImpl implements GroupClassMapper {

    @Override
    public GroupClassDTO toDTO(GroupClass groupClass) {
        if ( groupClass == null ) {
            return null;
        }

        Long classid = null;
        String classname = null;
        String instructor = null;
        String day = null;
        int duration = 0;
        int maxCapacity = 0;
        int currentCapacity = 0;
        List<ClassUserDTO> usersList = null;

        classid = groupClass.getClassid();
        classname = groupClass.getClassname();
        instructor = groupClass.getInstructor();
        if ( groupClass.getDay() != null ) {
            day = groupClass.getDay().name();
        }
        duration = groupClass.getDuration();
        maxCapacity = groupClass.getMaxCapacity();
        currentCapacity = groupClass.getCurrentCapacity();
        usersList = classUserListToClassUserDTOList( groupClass.getUsersList() );

        String timeInit = null;
        String timeFin = null;

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
        int duration = 0;
        int maxCapacity = 0;
        int currentCapacity = 0;

        classid = groupClass.getClassid();
        classname = groupClass.getClassname();
        instructor = groupClass.getInstructor();
        if ( groupClass.getDay() != null ) {
            day = groupClass.getDay().name();
        }
        duration = groupClass.getDuration();
        maxCapacity = groupClass.getMaxCapacity();
        currentCapacity = groupClass.getCurrentCapacity();

        String timeInit = null;
        String timeFin = null;

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
        groupClass.setDuration( groupClassDTO.duration() );
        groupClass.setMaxCapacity( groupClassDTO.maxCapacity() );
        groupClass.setCurrentCapacity( groupClassDTO.currentCapacity() );

        return groupClass;
    }

    protected List<GroupClassDTO> groupClassListToGroupClassDTOList(List<GroupClass> list) {
        if ( list == null ) {
            return null;
        }

        List<GroupClassDTO> list1 = new ArrayList<GroupClassDTO>( list.size() );
        for ( GroupClass groupClass : list ) {
            list1.add( toDTO( groupClass ) );
        }

        return list1;
    }

    protected PostDTO postToPostDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        long postid = 0L;
        ClassUserDTO creator = null;
        String title = null;
        String description = null;
        String imagePath = null;

        postid = post.getPostid();
        creator = classUserToClassUserDTO( post.getCreator() );
        title = post.getTitle();
        description = post.getDescription();
        imagePath = post.getImagePath();

        PostDTO postDTO = new PostDTO( postid, creator, title, description, imagePath );

        return postDTO;
    }

    protected List<PostDTO> postListToPostDTOList(List<Post> list) {
        if ( list == null ) {
            return null;
        }

        List<PostDTO> list1 = new ArrayList<PostDTO>( list.size() );
        for ( Post post : list ) {
            list1.add( postToPostDTO( post ) );
        }

        return list1;
    }

    protected ClassUserDTO classUserToClassUserDTO(ClassUser classUser) {
        if ( classUser == null ) {
            return null;
        }

        long userid = 0L;
        String name = null;
        List<GroupClassDTO> listOfClasses = null;
        List<PostDTO> listOfPosts = null;

        if ( classUser.getUserid() != null ) {
            userid = classUser.getUserid();
        }
        name = classUser.getName();
        listOfClasses = groupClassListToGroupClassDTOList( classUser.getListOfClasses() );
        listOfPosts = postListToPostDTOList( classUser.getListOfPosts() );

        ClassUserDTO classUserDTO = new ClassUserDTO( userid, name, listOfClasses, listOfPosts );

        return classUserDTO;
    }

    protected List<ClassUserDTO> classUserListToClassUserDTOList(List<ClassUser> list) {
        if ( list == null ) {
            return null;
        }

        List<ClassUserDTO> list1 = new ArrayList<ClassUserDTO>( list.size() );
        for ( ClassUser classUser : list ) {
            list1.add( classUserToClassUserDTO( classUser ) );
        }

        return list1;
    }
}
