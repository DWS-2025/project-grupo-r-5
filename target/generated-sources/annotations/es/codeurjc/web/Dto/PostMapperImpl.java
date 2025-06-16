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
    date = "2025-06-16T18:19:18+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public PostDTO toDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        ClassUserDTO creator = null;
        long postid = 0L;
        String title = null;
        String description = null;
        String imagePath = null;

        creator = classUserToClassUserDTO( post.getCreator() );
        postid = post.getPostid();
        title = post.getTitle();
        description = post.getDescription();
        imagePath = post.getImagePath();

        PostDTO postDTO = new PostDTO( postid, creator, title, description, imagePath );

        return postDTO;
    }

    @Override
    public List<PostDTO> toDTOs(Collection<Post> posts) {
        if ( posts == null ) {
            return null;
        }

        List<PostDTO> list = new ArrayList<PostDTO>( posts.size() );
        for ( Post post : posts ) {
            list.add( toDTO( post ) );
        }

        return list;
    }

    @Override
    public Post toDomain(PostDTO postDTO) {
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

    protected GroupClassDTO groupClassToGroupClassDTO(GroupClass groupClass) {
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

    protected List<GroupClassDTO> groupClassListToGroupClassDTOList(List<GroupClass> list) {
        if ( list == null ) {
            return null;
        }

        List<GroupClassDTO> list1 = new ArrayList<GroupClassDTO>( list.size() );
        for ( GroupClass groupClass : list ) {
            list1.add( groupClassToGroupClassDTO( groupClass ) );
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
        listOfPosts = toDTOs( classUser.getListOfPosts() );

        ClassUserDTO classUserDTO = new ClassUserDTO( userid, name, listOfClasses, listOfPosts );

        return classUserDTO;
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

    protected GroupClass groupClassDTOToGroupClass(GroupClassDTO groupClassDTO) {
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
            list1.add( groupClassDTOToGroupClass( groupClassDTO ) );
        }

        return list1;
    }

    protected List<Post> postDTOListToPostList(List<PostDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Post> list1 = new ArrayList<Post>( list.size() );
        for ( PostDTO postDTO : list ) {
            list1.add( toDomain( postDTO ) );
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
}
