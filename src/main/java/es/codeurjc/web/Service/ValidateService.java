package es.codeurjc.web.Service;

import es.codeurjc.web.Domain.GroupClass;
import es.codeurjc.web.Domain.Post;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.util.Objects;

@Service
public class ValidateService {

    //XSS protection implement with Jsoup:
    private static final Safelist CUSTOM_SAFE_LIST = Safelist.relaxed()
            .addTags("time", "br", "p", "h1", "h2", "h3", "h4", "h5", "h6", "strong", "em", "b", "i")
            .addAttributes("img", "src", "alt", "title")
            .addProtocols("img", "src", "http", "https")
            .addAttributes("a", "href", "title")
            .addProtocols("a", "href", "http", "https")
            .removeTags("script", "style", "iframe") //just in case, explicitly remove these tags
            .preserveRelativeLinks(true); //we accept relative paths

    private String cleanInput(String input){
        return Jsoup.clean(input, CUSTOM_SAFE_LIST);
    }


    //GroupClasses:
    public String validateName(String name){
        String cleanedName = cleanInput(name);
        if(cleanedName.isEmpty()){
            return "Debes escribir el nombre de la clase, no puede estar vacio";
        }
        else if(cleanedName.length() > 20) {
            return "El tamaño maximo del nombre es de 20 caracteres";
        }
        return null;
    }

    public String validateDay(DayOfWeek day) {
        String cleanedDay = cleanInput(String.valueOf(day));
        if (cleanedDay.isEmpty()) {
            return "Debes seleccionar un día para la clase";
        }

        //Verify if it's a valid day
        String[] allowedDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Any"};
        boolean validDay = false;
        for (String allowedDay : allowedDays) {
            if (day.equals(allowedDay)) {
                validDay = true;
                break;
            }
        }

        if (!validDay) {
            return "El día seleccionado no es válido";
        }
        return null; //Day valid, no error
    }

    public String cleanInstructor(String instructor){
        return Jsoup.clean(instructor, CUSTOM_SAFE_LIST);
    }

    public String cleanDay(DayOfWeek day){
        return Jsoup.clean(String.valueOf(day), CUSTOM_SAFE_LIST);
    }

    public String validateInstructor(String instructor){
        String cleanedInstructor = cleanInput(instructor);
        if(cleanedInstructor.isEmpty()){
            return "Debes escribir el nombre del instructor, no puede estar vacio";
        }

        else if(cleanedInstructor.length() > 20) {
            return "El tamaño maximo del nombre del instructor es de 20 caracteres";
        }
        return null;
    }


    public String validateHour(String time) {
        String cleanedTime = cleanInput(time);
        String timePattern = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        if(cleanedTime.isEmpty()){
            return "Debes introducir una hora para la clase";
        }

        if (!cleanedTime.matches(timePattern)) {
            return "El formato de la hora debe ser HH:MM (de 00:00 a 23:59)";
        }
        return null;
    }


    public String validateCapacity(String capacityStr){
        String cleanedCapacity = cleanInput(capacityStr);
        if (cleanedCapacity.isEmpty()) {
            return "Debes indicar el numero maximo de alumnos, no puede estar vacio";
        }

        try {
            int capacity = Integer.parseInt(cleanedCapacity);
            if (capacity <= 0) {
                return "Debe haber como minimo un alumno, no puede haber menos";
            }
        } catch (NumberFormatException e) {
            return "Solo se puede introducir numeros";
        }
        return null;
    }

    public String validateClass(GroupClass groupClass){
        String nameError = validateName(groupClass.getClassname());
        if(nameError != null){
            return nameError;
        }
        String dayError = validateDay(groupClass.getDay());
        if(dayError != null){
            return dayError;
        }
        String hourError = validateHour(groupClass.getTime_init().toString());
        if (hourError != null){
            return hourError;
        }
        String instructorError = validateInstructor(groupClass.getInstructor());
        if (instructorError != null){
            return instructorError;
        }
        String capacityError = validateCapacity(String.valueOf(groupClass.getMaxCapacity()));
        if (capacityError != null){
            return capacityError;
        }
        return null;
    }


    //Posts:
    public String validateUsername(String username){
        String cleanedUsername = cleanInput(username);
        if(cleanedUsername.isEmpty()){
            return "Debes escribir tu nombre de usuario";
        }
        else if(cleanedUsername.length() > 58){
            System.out.println("username:"+ cleanedUsername) ;
            return "El nombre debe de tener menos de 25 caracteres";
        }
        return null;
    }


    public String validateTitle(String title){
        String cleanedTitle = cleanInput(title);
        if(cleanedTitle.isEmpty()){
            return "Debes escribir un titulo, no puede estar vacio";
        }
        else if(cleanedTitle.length() > 50) {
            System.out.println("titulo:"+ cleanedTitle) ;
            return "El tamaño maximo del titulo es de 50 caracteres";
        }
        return null;
    }


    public String validateText(String text){
        String cleanedText = cleanInput(text);
        if(cleanedText.isEmpty()){
            return "Debes escribir algo en el post, no puedes dejar el campo Text vacio";
        }
        if(cleanedText.length() > 500) {
            return "El tamaño maximo del titulo es de 500 caracteres";
        }
        return null;
    }


    public String validateImage(MultipartFile image){
        if(!Objects.requireNonNull(image.getContentType()).startsWith("image/")){
            return "El archivo debe de ser una imagen";
        }
        return null;
    }

    public boolean isValidFileName(String fileName) {
        String cleanedFileName = cleanInput(fileName);
        return !cleanedFileName.isEmpty() && cleanedFileName.matches("[a-zA-Z0-9._-]+");
    }

    public String validatePost(Post post){
        String nameError = validateUsername(post.getCreatorName());
        if (nameError != null){
            return nameError;
        }
        String tittleError = validateTitle(post.getTitle());
        if (tittleError != null){
            return tittleError;
        }
        String textError = validateText(post.getDescription());
        if (textError != null){
            return textError;
        }
        return null;

    }


    public String validatePostWithImage(Post post){
        String nameError = validateUsername(post.getCreatorName());
        if (nameError != null){
            return nameError;
        }
        String tittleError = validateTitle(post.getTitle());
        if (tittleError != null){
            return tittleError;
        }
        String textError = validateText(post.getDescription());
        if (textError != null){
            return textError;
        }
        String imageNameError = String.valueOf(isValidFileName(post.getImagePath()));
        if(imageNameError != null){
            return imageNameError;
        }
        String imageError = validateImage((MultipartFile) post.getImageFile());
        if(imageError != null){
            return imageError;
        }
        return null;

    }

}
