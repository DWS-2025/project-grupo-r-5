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
    date = "2025-06-14T20:05:10+0200",
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

        classid = groupClass.getClassid();
        classname = groupClass.getClassname();
        instructor = groupClass.getInstructor();
        if ( groupClass.getDay() != null ) {
            day = groupClass.getDay().name();
        }
        duration = groupClass.getDuration();
        maxCapacity = groupClass.getMaxCapacity();
        currentCapacity = groupClass.getCurrentCapacity();

        List<ClassUserDTO> usersList = null;
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
        groupClass.setDuration( groupClassDTO.duration() );
        groupClass.setMaxCapacity( groupClassDTO.maxCapacity() );
        groupClass.setCurrentCapacity( groupClassDTO.currentCapacity() );
        groupClass.setUsersList( classUserDTOListToClassUserList( groupClassDTO.usersList() ) );

        return groupClass;
    }

    protected List<GroupClass> groupClassDTOListToGroupClassList(List<GroupClassDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<GroupClass> list1 = new ArrayList<GroupClass>( list.size() );
        for ( GroupClassDTO groupClassDTO : list ) {
            list1.add( toDomain( groupClassDTO ) );
        }

        return list1;
    }

    protected Post postDTOToPost(PostDTO postDTO) {
        if ( postDTO == null ) {
            return null;
        }

        Post post = new Post();

        post.setPostid( postDTO.postid() );
        post.setCreator( classUserDTOToClassUser( postDTO.creator() ) );
        post.setTitle( postDTO.title() );
        post.setDescription( postDTO.description() );
        post.setImagePath( postDTO.imagePath() );

        return post;
    }

    protected List<Post> postDTOListToPostList(List<PostDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Post> list1 = new ArrayList<Post>( list.size() );
        for ( PostDTO postDTO : list ) {
            list1.add( postDTOToPost( postDTO ) );
        }

        return list1;
    }

    protected ClassUser classUserDTOToClassUser(ClassUserDTO classUserDTO) {
        if ( classUserDTO == null ) {
            return null;
        }

        ClassUser classUser = new ClassUser();

        classUser.setUserid( classUserDTO.userid() );
        classUser.setName( classUserDTO.name() );
        classUser.setListOfClasses( groupClassDTOListToGroupClassList( classUserDTO.listOfClasses() ) );
        classUser.setListOfPosts( postDTOListToPostList( classUserDTO.listOfPosts() ) );

        return classUser;
    }

    protected List<ClassUser> classUserDTOListToClassUserList(List<ClassUserDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<ClassUser> list1 = new ArrayList<ClassUser>( list.size() );
        for ( ClassUserDTO classUserDTO : list ) {
            list1.add( classUserDTOToClassUser( classUserDTO ) );
        }

        return list1;
    }
}
