package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
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
public class ClassUserMapperImpl implements ClassUserMapper {

    @Override
    public ClassUserDTO toDTO(ClassUser classUser) {
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

    @Override
    public ClassUserBasicDTO toBasicDTO(ClassUser classUser) {
        if ( classUser == null ) {
            return null;
        }

        long userid = 0L;
        String name = null;

        if ( classUser.getUserid() != null ) {
            userid = classUser.getUserid();
        }
        name = classUser.getName();

        ClassUserBasicDTO classUserBasicDTO = new ClassUserBasicDTO( userid, name );

        return classUserBasicDTO;
    }

    @Override
    public List<ClassUserBasicDTO> toDTOs(Collection<ClassUser> classUsers) {
        if ( classUsers == null ) {
            return null;
        }

        List<ClassUserBasicDTO> list = new ArrayList<ClassUserBasicDTO>( classUsers.size() );
        for ( ClassUser classUser : classUsers ) {
            list.add( toBasicDTO( classUser ) );
        }

        return list;
    }

    @Override
    public ClassUser toDomain(ClassUserDTO classUserDTO) {
        if ( classUserDTO == null ) {
            return null;
        }

        ClassUser classUser = new ClassUser();

        classUser.setUserid( classUserDTO.userid() );
        classUser.setName( classUserDTO.name() );

        return classUser;
    }

    @Override
    public ClassUser toDomain(ClassUserBasicDTO classUserBasicDTO) {
        if ( classUserBasicDTO == null ) {
            return null;
        }

        ClassUser classUser = new ClassUser();

        classUser.setUserid( classUserBasicDTO.userid() );
        classUser.setName( classUserBasicDTO.name() );

        return classUser;
    }

    protected List<ClassUserDTO> classUserListToClassUserDTOList(List<ClassUser> list) {
        if ( list == null ) {
            return null;
        }

        List<ClassUserDTO> list1 = new ArrayList<ClassUserDTO>( list.size() );
        for ( ClassUser classUser : list ) {
            list1.add( toDTO( classUser ) );
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
        creator = toDTO( post.getCreator() );
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
}
