package com.csc1106.learnzenith.entities;
import java.sql.Date;
import java.sql.Time;

public class User {
    private int userID; //primary key
    private int roleID; //foreign key
    private String userName; //this can be used to store varchar data from sql (varchar)
    private String userPassword; //this can be used to store varchar data from sql (varchar)
    private Date joineddDate;
    private Time joinedTime;


    public User(int userID, int roleID, String userName, String userPassword, Date joineddDate, Time joinedTime) {
        this.userID = userID;
        this.roleID = roleID;
        this.userName = userName;
        this.userPassword = userPassword;
        this.joineddDate = joineddDate;
        this.joinedTime = joinedTime;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getRoleID() {
        return roleID;
    }
    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public Date getJoineddDate() {
        return joineddDate;
    }
    public void setJoineddDate(Date joineddDate) {
        this.joineddDate = joineddDate;
    }
    public Time getJoinedTime() {
        return joinedTime;
    }
    public void setJoinedTime(Time joinedTime) {
        this.joinedTime = joinedTime;
    }
}