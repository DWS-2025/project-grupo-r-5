package es.codeurjc.web.Domain;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupClass {
    //Properties:
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long classid;
    private String classname;
    private String instructor;
    @Column(name = "DayOfWeek")
    private DayOfWeek day;
    private LocalTime timeInit;
    private int duration;
    private LocalTime timeFin;
    private int maxCapacity;
    private int currentCapacity;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "groupclass_users",
            joinColumns = @JoinColumn(name = "classid"),
            inverseJoinColumns = @JoinColumn(name = "userid")
    )
    private List<ClassUser> usersList = new ArrayList<>();


    //Constructor:
    public GroupClass() {}
    public GroupClass(String name, DayOfWeek day, LocalTime timeInit,int duration, String instructor, int maxCapacity) {
        this.classname = name;
        this.day = day;
        this.timeInit = timeInit;
        this.duration = duration;
        this.timeFin = this.getTimeFin();
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
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
    public LocalTime getTimeInit() {
        return timeInit;
    }
    public void setTimeInit(LocalTime timeInit) {
        this.timeInit = timeInit;
    }
    public String getTimeInitAsString() {
        return this.timeInit.toString();
    }

    //Duration
    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    //Time_fin
    public LocalTime getTimeFin() {
        if(timeInit != null){return timeInit.plusMinutes(this.duration);}
        return null;
    }
    public String getTimeFinAsString(){
        return this.getTimeFin().toString();
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


    //User
    public List<ClassUser> getUsersList() {
        return this.usersList;
    }
    public void setUsersList(List<ClassUser>usersList) {
        this.usersList = usersList;
    }


}
