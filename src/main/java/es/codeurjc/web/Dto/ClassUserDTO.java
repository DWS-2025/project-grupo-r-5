package es.codeurjc.web.Dto;

import java.util.List;

public record ClassUserDTO(
        long userid,
        String username,
        List<GroupClassBasicDTO> listOfClasses,
        List<PostDTO> listOfPosts
) {
}
