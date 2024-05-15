package com.csc1106.learnzenith.entities;

public class CourseForum {
    
    private int courseID;
    private int numOfThreads;
    private ForumThread[] threads;

    public CourseForum(int courseID, int numOfThreads, ForumThread[] threads) {
        this.courseID = courseID;
        this.numOfThreads = numOfThreads;
        this.threads = threads;
    }
    
    public int getCourseID() {
        return courseID;
    }
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
    public int getNumOfThreads() {
        return numOfThreads;
    }
    public void setNumOfThreads(int numOfThreads) {
        this.numOfThreads = numOfThreads;
    }
    public ForumThread[] getThreads() {
        return threads;
    }
    public void setThreads(ForumThread[] threads) {
        this.threads = threads;
    }
}
