package es.codeurjc.web.Service;


import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.repository.GroupClassRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GroupClassService {

    @Autowired
    private GroupClassRepository groupClassRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserService userService;


    private AtomicLong nextId = new AtomicLong(1L);

    public List<GroupClass> findAll() {return groupClassRepository.findAll();}

    public Map<DayOfWeek, List<GroupClass>> getClassesGroupedByDayAndSortedByTime() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GroupClass> query = cb.createQuery(GroupClass.class);
        Root<GroupClass> root = query.from(GroupClass.class);

        // Ordenar por día de la semana y luego por hora
        query.select(root)
                .orderBy(cb.asc(root.get("day")), cb.asc(root.get("time")));

        TypedQuery<GroupClass> typedQuery = entityManager.createQuery(query);
        List<GroupClass> allClasses = typedQuery.getResultList();

        // Agrupar en un mapa donde la clave es el DayOfWeek
        Map<DayOfWeek, List<GroupClass>> groupedClasses = new LinkedHashMap<>();

        for (GroupClass groupClass : allClasses) {
            groupedClasses
                    .computeIfAbsent(groupClass.getDay(), k -> new ArrayList<>()) // Crea la lista si no existe
                    .add(groupClass);
        }

        return groupedClasses;
    }

    public GroupClass save(GroupClass groupClass) {
        long id = nextId.getAndIncrement();
        groupClass.setClassid(id);
        groupClassRepository.save(groupClass);
        return groupClass;
    }

}
