package com.csc1106.learnzenith.entities;

public class Course {
    private int courseID;
    private String courseTitle;
    private String courseDescription;
    private Double coursePrice;
    private Double courseDuration;
    private String courseLevel;

    public Course(int courseID, String courseTitle, String courseDescription, Double coursePrice, Double courseDuration, String courseLevel) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.coursePrice = coursePrice;
        this.courseDuration = courseDuration;
        this.courseLevel = courseLevel;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public Double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(Double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Double getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(Double courseDuration) {
        this.courseDuration = courseDuration;
    }

    public String getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", coursePrice=" + coursePrice +
                ", courseDuration=" + courseDuration +
                ", courseLevel='" + courseLevel + '\'' +
                '}';
    }
    
}
