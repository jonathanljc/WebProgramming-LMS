package com.csc1106.learnzenith.entities;

import java.sql.Date;
import java.sql.Time;

public class ReviewAndRating {
    private int reviewID;
    private int courseID;
    private int userID;
    private String review;
    private int rating;
    private Date reviewDate;
    private Time reviewTime;

    public ReviewAndRating(int reviewID, int courseID, int userID, String review, int rating, Date reviewDate, Time reviewTime) {
        this.reviewID = reviewID;
        this.courseID = courseID;
        this.userID = userID;
        this.review = review;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.reviewTime = reviewTime;
    }
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }
    public int getReviewID() {
        return reviewID;
    }
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
    public int getCourseID() {
        return courseID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return userID;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public String getReview() {
        return review;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public int getRating() {
        return rating;
    }
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
    public Date getReviewDate() {
        return reviewDate;
    }
    public void setReviewTime(Time reviewTime) {
        this.reviewTime = reviewTime;
    }
    public Time getReviewTime() {
        return reviewTime;
    }

}
