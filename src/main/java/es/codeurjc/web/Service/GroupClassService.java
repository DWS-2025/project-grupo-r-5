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
import java.util.*;
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

    public List<Map.Entry<String, List<GroupClass>>> getClassesGroupedByDayAndSortedByTime() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GroupClass> query = cb.createQuery(GroupClass.class);
        Root<GroupClass> root = query.from(GroupClass.class);

        // Ordenar por día de la semana y luego por hora
        query.select(root)
                .orderBy(cb.asc(root.get("day")), cb.asc(root.get("time_init")));

        TypedQuery<GroupClass> typedQuery = entityManager.createQuery(query);
        List<GroupClass> allClasses = typedQuery.getResultList();

        // Agrupar en un mapa con String como clave
        Map<String, List<GroupClass>> groupedClasses = new LinkedHashMap<>();

        for (GroupClass groupClass : allClasses) {
            String dayAsString = groupClass.getDay().toString();
            groupedClasses
                    .computeIfAbsent(dayAsString, k -> new ArrayList<>())
                    .add(groupClass);
        }

        // Convertimos el mapa a una lista de entradas
        return new ArrayList<>(groupedClasses.entrySet());
    }

    public GroupClass findGroupClassById(long id){
        return groupClassRepository.findById(id).orElseThrow();
    }
    public boolean exist(long id){return groupClassRepository.existsById(id);}
    public Optional<GroupClass> findById(long id) {

        if(this.exist(id)){
            return Optional.of(this.findGroupClassById(id));
        }
        return Optional.empty();

    }

    public GroupClass save(GroupClass groupClass) {
        long id = nextId.getAndIncrement();
        groupClass.setClassid(id);
        groupClassRepository.save(groupClass);
        return groupClass;
    }

}
