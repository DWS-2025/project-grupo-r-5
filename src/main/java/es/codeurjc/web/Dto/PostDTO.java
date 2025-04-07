package es.codeurjc.web.Dto;

import com.mysql.cj.jdbc.Blob;
import es.codeurjc.web.Domain.ClassUser;

public record PostDTO (
    long postid,
    ClassUser creator,
    String title,
    String description,
    String imagePath){
}
