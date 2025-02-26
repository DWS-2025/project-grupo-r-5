package es.codeurjc.web.Model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GroupClass {
    //Properties:
    private Long classid; //More efficient
    private String classname;
    private String instructor;
    private DayOfWeek day;
    private LocalTime time_init;
    private int duration;
    private LocalTime time_fin;
    private int maxCapacity;
    private int currentCapacity;
    private boolean officialClass;
    private List<ClassUser> usersList = new ArrayList<>();


    //Constructor:
    public GroupClass() {}
    public GroupClass(String name, DayOfWeek day, LocalTime time_init,int duration, String instructor, int maxCapacity, boolean officialClass) {
        this.classname = name;
        this.day = day;
        this.time_init = time_init;
        this.duration = duration;
        this.time_fin = this.getTimefin();
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.officialClass = officialClass;
        this.usersList = new ArrayList<>();
    }

    //Methods:
    public boolean isFull(){
        return this.currentCapacity == this.maxCapacity;
    }
    public boolean addUser(ClassUser classUser) {
        if(this.isFull()){
            return false;
        } else{
            this.currentCapacity++;
            return this.usersList.add(classUser);
        }
    }
    public boolean removeUser(ClassUser classUser) {
        if(this.currentCapacity > 0){
            this.currentCapacity--;
            return this.usersList.remove(classUser);
        }
        return false;
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
    public String getDayAsString() {
        return this.day.toString();
    }

    //Time_init
    public LocalTime getTime_init() {
        return time_init;
    }
    public void setTime_init(LocalTime time) {
        this.time_init = time;
    }
    public String getTimeAsString() {
        return this.time_init.toString();
    }

    //Duration
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    //Time_fin
    public LocalTime getTimefin() {
        return time_init.plusMinutes(this.duration);
    }
    public String getTimefinAsString(){
        return this.getTimefin().toString();
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
    public List<ClassUser> getUsersList() {
        return this.usersList;
    }
    public void setUsersList(List<ClassUser>usersList) {
        this.usersList = usersList;
    }


}
