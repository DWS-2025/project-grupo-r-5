package es.codeurjc.web.Service;

import es.codeurjc.web.Model.GroupClass;
import es.codeurjc.web.Model.Post;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ValidateService {

    //XSS protection implement with Jsoup
    public String cleanInput(String input){
        return Jsoup.clean(input, Safelist.relaxed());
    }


    public String validateName(String name){
        if(name == null || name.isEmpty()){
            return "Debes escribir el nombre de la clase, no puede estar vacio";
        }

        else if(name.length() > 20) {
            return "El tamaño maximo del nombre es de 20 caracteres";
        }
    return null;
    }
    public String validateDay(String day) {
        if (day == null || day.isEmpty()) {
            return "Debes seleccionar un día para la clase";
        }

        // Verify if it's a valid day
        String[] allowedDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday", "Any"};
        boolean validDay = false;
        for (String allowedDay : allowedDays) {
            if (day.equalsIgnoreCase(allowedDay)) {
                validDay = true;
                break;
            }
        }

        if (!validDay) {
            return "El día seleccionado no es válido";
        }

        return null; // Day valid, no error
    }
    public String validateInstructor(String instructor){
        if(instructor == null || instructor.isEmpty()){
            return "Debes escribir el nombre del instructor, no puede estar vacio";
        }

        else if(instructor.length() > 20) {
            return "El tamaño maximo del nombre del instructor es de 20 caracteres";
        }
        return null;
    }
    public String validateHour(String time) {

        String timePattern = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";
        if(time == null || time.isEmpty()){
            return "Debes introducir una hora para la clase";
        }

        if (!time.matches(timePattern)) {
            return "El formato de la hora debe ser HH:MM (de 00:00 a 23:59)";
        }

        return null;
    }
    public String validateCapacity(String capacityStr){

        if (capacityStr == null || capacityStr.isEmpty()) {
            return "Debes indicar el numero maximo de alumnos, no puede estar vacio";
        }

        try {
            int capacity = Integer.parseInt(capacityStr);
            if (capacity <= 0) {
                return "Debe haber como minimo un alumno, no puede haber menos";
            }
        } catch (NumberFormatException e) {
            return "Solo se puede introducir numeros";
        }
        return null;
    }
    public String validateUsername(String username){
        if(username == null || username.isEmpty()){
            return "Debes escribir tu nombre de usuario";
        }
        else if(username.length() > 58){
            System.out.println("username:"+ username) ;
            return "El nombre debe de tener menos de 25 caracteres";
        }
        return null;
    }
    public String validateTittle(String title){
        if(title == null || title.isEmpty()){
            return "Debes escribir un titulo, no puede estar vacio";
        }
        else if(title.length() > 50) {
            System.out.println("titulo:"+ title) ;
            return "El tamaño maximo del titulo es de 50 caracteres";
        }
        return null;
    }
    public String validateText(String text){
        if(text == null || text.isEmpty()){
            return "Debes escribir algo en el post, no puedes dejar el campo Text vacio";
        }
        if(text.length() > 500) {
            return "El tamaño maximo del titulo es de 500 caracteres";
        }
        return null;
    }

    //Uncomment this in the future
    /*
    public String validateClass(GroupClass groupClass){
        String nameError = validateName(groupClass.getName());
        if(nameError != null){
            return nameError;
        }
        String dayError = validateDay(groupClass.getDay());
        if(dayError != null){
            return dayError;
        }
        String hourError = validateHour(groupClass.getTime());
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
    */

    public String validateImage(MultipartFile image){
        if(!Objects.requireNonNull(image.getContentType()).startsWith("image/")){
            return "El archivo debe de ser una imagen";
        }
        return null;
    }

    public String validatePost(Post post){
        String nameError = validateUsername(post.getCreatorName());
        if (nameError != null){
            return nameError;
        }
        String tittleError = validateTittle(post.getTitle());
        if (tittleError != null){
            return tittleError;
        }
        String textError = validateText(post.getDescription());
        if (textError != null){
            return textError;
        }
        return null;

    }

    public boolean isValidFileName(String fileName) {
        return fileName != null && !fileName.isEmpty() && fileName.matches("[a-zA-Z0-9._-]+");
    }

}
