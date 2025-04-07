package es.codeurjc.web.Dto;

import java.util.List;

public record GroupClassBasicDTO(
        Long classid,
        String classname,
        String instructor,
        String day,
        String timeInit,
        int duration,
        String timeFin,
        int maxCapacity,
        int currentCapacity
) {
}
