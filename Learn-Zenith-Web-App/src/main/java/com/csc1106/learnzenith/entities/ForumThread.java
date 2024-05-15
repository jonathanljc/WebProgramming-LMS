package com.csc1106.learnzenith.entities;

import java.sql.Date;
import java.sql.Time;

public class ForumThread {

    private int threadID; // primary key
    private int courseID; // foreign key to course table
    private String posterName; // name of user that posted the thread
    private Date postDate; 
    private Time postTime;
    private String title;
    private String content;

    public ForumThread(int threadID, int courseID, String posterName, Date postDate, Time postTime, int replies,
            String title, String subject) {
        this.threadID = threadID;
        this.courseID = courseID;
        this.posterName = posterName;
        this.postDate = postDate;
        this.postTime = postTime;
        this.title = title;
        this.content = subject;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Time getPostTime() {
        return postTime;
    }

    public void setPostTime(Time postTime) {
        this.postTime = postTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String subject) {
        this.content = subject;
    }
    
}
