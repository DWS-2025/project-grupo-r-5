package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T23:50:15+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class ClassUserMapperImpl implements ClassUserMapper {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private GroupClassMapper groupClassMapper;

    @Override
    public ClassUserDTO toDTO(ClassUser classUser) {
        if ( classUser == null ) {
            return null;
        }

        String username = null;
        long userid = 0L;
        List<String> roles = null;
        List<GroupClassBasicDTO> listOfClasses = null;
        List<PostDTO> listOfPosts = null;

        username = classUser.getUsername();
        if ( classUser.getUserid() != null ) {
            userid = classUser.getUserid();
        }
        List<String> list = classUser.getRoles();
        if ( list != null ) {
            roles = new ArrayList<String>( list );
        }
        listOfClasses = groupClassMapper.toDTOs( classUser.getListOfClasses() );
        listOfPosts = postMapper.toDTOs( classUser.getListOfPosts() );

        ClassUserDTO classUserDTO = new ClassUserDTO( userid, username, roles, listOfClasses, listOfPosts );

        return classUserDTO;
    }

    @Override
    public ClassUserBasicDTO toBasicDTO(ClassUser classUser) {
        if ( classUser == null ) {
            return null;
        }

        String username = null;
        long userid = 0L;

        username = classUser.getUsername();
        if ( classUser.getUserid() != null ) {
            userid = classUser.getUserid();
        }

        ClassUserBasicDTO classUserBasicDTO = new ClassUserBasicDTO( userid, username );

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
        classUser.setUsername( classUserDTO.username() );
        List<String> list = classUserDTO.roles();
        if ( list != null ) {
            classUser.setRoles( new ArrayList<String>( list ) );
        }
        classUser.setListOfClasses( groupClassBasicDTOListToGroupClassList( classUserDTO.listOfClasses() ) );
        classUser.setListOfPosts( postDTOListToPostList( classUserDTO.listOfPosts() ) );

        return classUser;
    }

    @Override
    public ClassUser toDomain(ClassUserBasicDTO classUserBasicDTO) {
        if ( classUserBasicDTO == null ) {
            return null;
        }

        ClassUser classUser = new ClassUser();

        classUser.setUserid( classUserBasicDTO.userid() );
        classUser.setUsername( classUserBasicDTO.username() );

        return classUser;
    }

    protected List<GroupClass> groupClassBasicDTOListToGroupClassList(List<GroupClassBasicDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<GroupClass> list1 = new ArrayList<GroupClass>( list.size() );
        for ( GroupClassBasicDTO groupClassBasicDTO : list ) {
            list1.add( groupClassMapper.toDomain( groupClassBasicDTO ) );
        }

        return list1;
    }

    protected List<Post> postDTOListToPostList(List<PostDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Post> list1 = new ArrayList<Post>( list.size() );
        for ( PostDTO postDTO : list ) {
            list1.add( postMapper.toDomain( postDTO ) );
        }

        return list1;
    }
}
