package es.codeurjc.web.Dto;

import es.codeurjc.web.Domain.Post;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-26T15:36:21+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Autowired
    private ClassUserMapper classUserMapper;

    @Override
    public PostDTO toDTO(Post post) {
        if ( post == null ) {
            return null;
        }

        ClassUserBasicDTO creator = null;
        long postid = 0L;
        String title = null;
        String description = null;
        String imagePath = null;

        creator = classUserMapper.toBasicDTO( post.getCreator() );
        postid = post.getPostid();
        title = post.getTitle();
        description = post.getDescription();
        imagePath = post.getImagePath();

        PostDTO postDTO = new PostDTO( postid, creator, title, description, imagePath );

        return postDTO;
    }

    @Override
    public List<PostDTO> toDTOs(Collection<Post> posts) {
        if ( posts == null ) {
            return null;
        }

        List<PostDTO> list = new ArrayList<PostDTO>( posts.size() );
        for ( Post post : posts ) {
            list.add( toDTO( post ) );
        }

        return list;
    }

    @Override
    public Post toDomain(PostDTO postDTO) {
        if ( postDTO == null ) {
            return null;
        }

        Post post = new Post();

        post.setPostid( postDTO.postid() );
        post.setCreator( classUserMapper.toDomain( postDTO.creator() ) );
        post.setTitle( postDTO.title() );
        post.setDescription( postDTO.description() );
        post.setImagePath( postDTO.imagePath() );

        return post;
    }
}
