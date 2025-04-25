package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassUserMapper {

    ClassUserDTO toDTO(ClassUser classUser);

    List<ClassUserBasicDTO> toDTOs(Collection<ClassUser> classUsers);

    @Mapping(target = "listOfClasses", ignore = true)
    @Mapping(target = "listOfPosts", ignore = true)
    ClassUser toDomain(ClassUserDTO classUserDTO);
    ClassUser toDomain(ClassUserBasicDTO classUserBasicDTO);
}
