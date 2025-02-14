package es.codeurjc.web.Model;

import com.fasterxml.jackson.annotation.JsonTypeId;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class GroupClass {
    //Properties:
    @JsonTypeId
    private Long classid; //More efficient
    private String classname;
    private String instructor;
    private DayOfWeek day;
    private LocalTime time;
    private int maxCapacity;
    private int currentCapacity;
    private boolean officialClass;
    private Set<User> userInClass = new HashSet<>(); //to avoid duplicates


    //Constructor:
    public GroupClass(String name, DayOfWeek day, LocalTime time, String instructor, int maxCapacity, boolean officialClass) {
        this.classname = name;
        this.day = day;
        this.time = time;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.officialClass = officialClass;
    }

    //Methods:
    public boolean isFull(){
        return this.currentCapacity == this.maxCapacity;
    }
    public boolean addUser(User user) {
        if(this.isFull()){
            return false;
        } else{
            return userInClass.add(user);
        }
    }
    public boolean removeUser(User user) {
        return userInClass.remove(user);
    }


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

        //Time
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
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
    public Set<User> getUserInClass() {
        return userInClass;
    }
    public void setUserInClass(Set<User> userInClass) {
        this.userInClass = userInClass;
    }
}
