package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class GroupClass {
    //Properties:
    @Id @JsonTypeId
    private Long classid; //More efficient
    private String classname;
    private String instructor;
    @Column(name = "day_of_week") // It's a reservated name in H2.
    private DayOfWeek day;
    private LocalTime time;
    private int maxCapacity;
    private int currentCapacity;
    private boolean officialClass;
    //In the future when needed this will be uncommented
    //private Set<ClassUser> classUserInClasses = new HashSet<>(); //to avoid duplicates


    //Constructor:
    public GroupClass() {}
    public GroupClass(String name, DayOfWeek day, LocalTime time, String instructor, int maxCapacity, boolean officialClass) {
        this.classname = name;
        this.day = day;
        this.time = time;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.officialClass = officialClass;
    }

    //Methods:
    //In the future when needed this will be uncommented
    public boolean isFull(){
        return this.currentCapacity == this.maxCapacity;
    }
    //In the future when needed this will be uncommented
    /*
    public boolean addUser(ClassUser classUser) {
        if(this.isFull()){
            return false;
        } else{
            return classUserInClasses.add(classUser);
        }
    }
    public boolean removeUser(ClassUser classUser) {
        return classUserInClasses.remove(classUser);
    }*/


    //Getters & setters:
        //Id
    public Long getClassid() {
        return classid;
    }
    public void setClassid(Long classid) {
        this.classid = classid;
    }

        //Name
    public String getClassname() {
        return classname;
    }
    public void setClassname(String classname) {
        this.classname = classname;
    }

        //Instructor
    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

        //Day
    public DayOfWeek getDay() {
        return day;
    }
    public void setDay(DayOfWeek day) {
        this.day = day;
    }
    public String getDayAsString() {
        return this.day.toString();
    }

        //Time
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public String getTimeAsString() {
        return this.time.toString();
    }

        //MaxCapacity
    public int getMaxCapacity() {
        return maxCapacity;
    }
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

        //CurrentCapacity
    public int getCurrentCapacity() {
        return currentCapacity;
    }
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

        //OfficialClass
    public boolean isOfficialClass() {
        return officialClass;
    }
    public void setOfficialClass(boolean officialClass) {
        this.officialClass = officialClass;
    }

        //User
        //In the future when needed this will be uncommented
    /*
    public Set<ClassUser> getUserInClass() {
        return classUserInClasses;
    }
    public void setUserInClass(Set<ClassUser> classUserInClasses) {
        this.classUserInClasses = classUserInClasses;
    }
     */
}
