package es.codeurjc.web.Dto;

import java.time.LocalTime;
import java.util.List;

public record GroupClassBasicDTO(
        Long classid,
        String classname,
        String instructor,
        String day,
        LocalTime timeInit,
        int duration,
        LocalTime timeFin,
        int maxCapacity,
        int currentCapacity
) {
}
