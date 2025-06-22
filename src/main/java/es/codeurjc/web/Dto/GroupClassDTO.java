package es.codeurjc.web.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.List;

public record GroupClassDTO(
        Long classid,
        String classname,
        String instructor,
        String day,
        LocalTime timeInit,
        @JsonProperty("duration") int duration,
        LocalTime timeFin,
        int maxCapacity,
        int currentCapacity,
        List<ClassUserDTO> usersList
) {
}

