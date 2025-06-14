package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "creator", source = "creator")
    PostDTO toDTO(Post post);

    //Page<PostDTO> toDTOs(Collection<Post> posts);
    List<PostDTO> toDTOs(Collection<Post> posts);

    @Mapping(target = "imageFile", ignore = true)
    Post toDomain(PostDTO postDTO);

}
