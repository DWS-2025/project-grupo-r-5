package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
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
public class ClassUserMapperImpl implements ClassUserMapper {

    @Override
    public ClassUserDTO toDTO(ClassUser classUser) {
        if ( classUser == null ) {
            return null;
        }

        long userid = 0L;
        String name = null;

        if ( classUser.getUserid() != null ) {
            userid = classUser.getUserid();
        }
        name = classUser.getName();

        List<GroupClassDTO> listOfClasses = null;
        List<PostDTO> listOfPosts = null;

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
}
