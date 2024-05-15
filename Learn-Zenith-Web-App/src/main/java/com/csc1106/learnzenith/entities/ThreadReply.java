package com.csc1106.learnzenith.entities;

import java.sql.Date;
import java.sql.Time;

public class ThreadReply {
    
    private int replyID; // primary key
    private int commentID; // replyID of comment that is being replied to
    private int threadID; // foreign key to thread table
    private String responderName; // name of user that posted the thread
    private Date replyDate;
    private Time replyTime;
    private String replyContent;

    public ThreadReply(int replyID, int commentID, int threadID, String responderName, Date replyDate, Time replyTime,
            String replyContent) {
        this.replyID = replyID;
        this.commentID = commentID;
        this.threadID = threadID;
        this.responderName = responderName;
        this.replyDate = replyDate;
        this.replyTime = replyTime;
        this.replyContent = replyContent;
    }

    public int getReplyID() {
        return replyID;
    }

    public void setReplyID(int replyID) {
        this.replyID = replyID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }

    public String getResponderName() {
        return responderName;
    }

    public void setResponderName(String responderName) {
        this.responderName = responderName;
    }

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public Time getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Time replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

}
