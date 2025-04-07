package es.codeurjc.web.Dto;

import java.util.List;

public record ClassUserDTO(
        long userid,
        String name,
        List<GroupClassDTO> listOfClasses,
        List<PostDTO> listOfPosts
) {
}
