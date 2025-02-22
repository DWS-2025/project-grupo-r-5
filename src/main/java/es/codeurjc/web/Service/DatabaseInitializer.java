package es.codeurjc.web.Service;

import es.codeurjc.web.Model.ClassUser;
import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;


@Component
public class DatabaseInitializer {

    @Autowired
    private GroupClassService groupClass;

    @Autowired
    private UserService users;

    @Autowired
    private PostService post;


    @PostConstruct
    public void init()throws IOException {

        ClassUser user1 = new ClassUser("Juan");
        ClassUser user2 = new ClassUser("María");
        ClassUser user3 = new ClassUser("Pedro");
        users.save(user1);
        users.save(user2);
        users.save(user3);

        GroupClass class1 = new GroupClass("Advanced yoga", DayOfWeek.MONDAY, LocalTime.parse("10:00"),60, "Professor A", 20, true);
        GroupClass class2 = new GroupClass("Pilates", DayOfWeek.TUESDAY, LocalTime.parse("15:00"),120, "Professor B", 15, true);
        GroupClass class3 = new GroupClass("CrossFit", DayOfWeek.WEDNESDAY, LocalTime.parse("18:00"),45, "Professor C", 25, true);
        GroupClass class4 = new GroupClass("Zumba", DayOfWeek.THURSDAY, LocalTime.parse("12:00"),60, "Professor D", 30, true);
        GroupClass class5 = new GroupClass("Spinning", DayOfWeek.FRIDAY, LocalTime.parse("17:00"),60, "Professor E", 20, true);
        GroupClass class6 = new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("09:00"),60, "Professor F", 25, true);
        GroupClass class7 = new GroupClass("Aerobics", DayOfWeek.SATURDAY, LocalTime.parse("10:00"),60, "Professor F", 25, true);

        groupClass.save(class1);
        groupClass.save(class2);
        groupClass.save(class3);
        groupClass.save(class4);
        groupClass.save(class5);
        groupClass.save(class6);
        groupClass.save(class7);


        Post post1 = new Post(user1, "Pedaleando al ritmo de la música", "¡Menuda clase de spinning! \uD83D\uDEB4\u200D♀\uFE0F\uD83D\uDCA8 La música y la energía del instructor hicieron que la hora pasara volando. Nos hizo subir la resistencia poco a poco hasta acabar con un sprint final que me dejó sin aliento. \uD83D\uDCA6\uD83D\uDD25 Ahora me duelen las piernas, pero la sensación de haberlo dado todo no tiene precio. ¡Repetiré seguro! \uD83D\uDCAA #SpinningVibes #CardioTotal");
        Post post2 = new Post(user2, "Desconexión total en la clase de yoga", "Hoy probé la clase de yoga y salí como nuevo. \uD83E\uDDD8\u200D♂\uFE0F\uD83D\uDC99 La instructora nos guió con mucha paciencia y nos ayudó a mejorar la postura en cada asana. Al principio me costó concentrarme, pero al final sentí una paz increíble. ¡Definitivamente volveré la próxima semana! \uD83D\uDE4C #YogaTime #Relajación #Bienestar");
        Post post3 = new Post(user3, "Nunca había sudado tanto \uD83D\uDE05", "¡La clase de CrossFit de hoy fue brutal! \uD83D\uDCA5 Sentía que no iba a poder con tantas repeticiones, pero la motivación del grupo me empujó a seguir. Hicimos sentadillas con peso, burpees y sprints... ¡Acabé agotado pero feliz! \uD83D\uDCAA\uD83D\uDCAF Si alguien busca un reto de verdad, esta clase es la mejor opción. #CrossFitLovers #NoPainNoGain");
        Post post4 = new Post(user2, "Bailar y entrenar al mismo tiempo, lo mejor", "Hoy fue mi primera clase de zumba y ¡me encantó! \uD83D\uDE0D\uD83C\uDFB6 Al principio me sentí un poco perdido con los pasos, pero el ambiente era tan divertido que pronto me solté. Una hora bailando sin parar y sin darme cuenta de que estaba haciendo ejercicio. ¡Recomendado 100%! \uD83D\uDD25 #ZumbaLovers #EjercicioDivertido");
        Post post5 = new Post(user1, "Energía al máximo", "No sabía que una clase de aerobics podía ser TAN intensa. \uD83D\uDE05\uD83D\uDD25 Saltos, movimientos rápidos y mucha coordinación… ¡pero qué bien se siente después! La música te motiva a seguir y el instructor hace que no quieras rendirte. Definitivamente es una de las mejores maneras de quemar calorías sin aburrirse. \uD83D\uDE03 #AerobicsPower #EnergíaPura");
        Post post6 = new Post(user2, "Fortaleciendo el cuerpo con Pilates", "Hoy fui a mi primera clase de pilates y me sorprendió lo exigente que puede ser. \uD83E\uDD2F\uD83D\uDCAA Pensé que sería solo estiramientos, pero trabajamos fuerza, control y respiración de una forma increíble. Me encantó cómo al final todo el cuerpo se siente más ligero y fuerte al mismo tiempo. ¡Definitivamente seguiré viniendo! \uD83E\uDDD8\u200D♀\uFE0F #PilatesLovers #FuerzaYFlexibilidad");

        post.save(post1);
        post.save(post2);
        post.save(post3);
        post.save(post4);
        post.save(post5);
        post.save(post6);

    }

}
