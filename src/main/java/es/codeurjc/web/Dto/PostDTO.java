package es.codeurjc.web.Dto;

import com.mysql.cj.jdbc.Blob;
import es.codeurjc.web.Domain.ClassUser;

public record PostDTO (
    long postid,
    ClassUserDTO creator,
    String title,
    String description,
    String imagePath){

}
