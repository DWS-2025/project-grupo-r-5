package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.GroupClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupClassMapper {

    @Mapping(target = "timeFin", expression = "java(groupClass.getTimeFin())")
    GroupClassDTO toDTO(GroupClass groupClass);

    GroupClassBasicDTO toBasicDTO(GroupClass groupClass);

    List<GroupClassBasicDTO> toDTOs(Collection<GroupClass> groupClasses);

    GroupClass toDomain(GroupClassBasicDTO groupClassDTO);

    GroupClass toDomain(GroupClassDTO groupClassDTO);
}
