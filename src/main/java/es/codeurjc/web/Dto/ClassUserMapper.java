package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.ClassUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = {PostMapper.class, GroupClassMapper.class})
public interface ClassUserMapper {

    @Mapping(target = "listOfClasses", ignore = true)
    @Mapping(target = "listOfPosts", ignore = true)
    ClassUserDTO toDTO(ClassUser classUser);

    ClassUserBasicDTO toBasicDTO(ClassUser classUser);

    List<ClassUserBasicDTO> toDTOs(Collection<ClassUser> classUsers);

    @Mapping(target = "listOfClasses", ignore = true)
    @Mapping(target = "listOfPosts", ignore = true)
    ClassUser toDomain(ClassUserDTO classUserDTO);
    ClassUser toDomain(ClassUserBasicDTO classUserBasicDTO);

    // Extra for pageable:
    default Page<ClassUserBasicDTO> toDTOsPage(Page<ClassUser> usersPage) {
        return usersPage.map(this::toBasicDTO);
    }
}
