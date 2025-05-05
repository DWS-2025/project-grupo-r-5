package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.GroupClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupClassMapper {
    GroupClassDTO toDTO(GroupClass groupClass);

    GroupClassBasicDTO toBasicDTO(GroupClass groupClass);

    List<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses);

    //@Mapping(target = "usersList" , ignore = true)
    GroupClass toDomain(GroupClassBasicDTO groupClassDTO);

    //GroupClass toDomain(GroupClassDTO groupClassDTO);
}
