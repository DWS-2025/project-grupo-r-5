package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.GroupClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupClassMapper {
    @Mapping(target = "usersList", ignore = true)
    GroupClassDTO toDTO(GroupClass groupClass);

    GroupClassBasicDTO toBasicDTO(GroupClass groupClass);

    //Page<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses);
    List<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses);

    @Mapping(target = "usersList" , ignore = true)
    GroupClass toDomain(GroupClassBasicDTO groupClassDTO);

    GroupClass toDomain(GroupClassDTO groupClassDTO);
}
