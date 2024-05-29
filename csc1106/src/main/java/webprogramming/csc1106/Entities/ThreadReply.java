package webprogramming.csc1106.Entities;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ThreadReply {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int replyID; // primary key

    @ManyToOne
    @JoinColumn(name = "fk_thread")
    private ForumThread thread;

    @OneToMany(mappedBy = "parent")
    private List<ThreadReply> childReplies;

    @ManyToOne
    @JoinColumn(name = "fk_parent", nullable = true)
    private ThreadReply parent; // the parent reply, can be null
    
    private String responderName; // name of user that posted the thread
    private Date replyDate;
    private Time replyTime;
    private String replyContent;

    // need to map to ForumThread(threadID)
    // private int threadID; // foreign key to thread table

    public int getReplyID() {
        return replyID;
    }

    // public void setReplyID(int replyID) {
    //     this.replyID = replyID;
    // }

    // public int getCommentID() {
    //     return commentID;
    // }

    // public void setCommentID(int commentID) {
    //     this.commentID = commentID;
    // }

    // public int getThreadID() {
    //     return threadID;
    // }

    // public void setThreadID(int threadID) {
    //     this.threadID = threadID;
    // }

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

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    public ThreadReply getParent() {
        return parent;
    }

    public void setParent(ThreadReply parent) {
        this.parent = parent;
    }
    
    public List<ThreadReply> getChildReplies() {
        return childReplies;
    }

    public void setChildReplies(List<ThreadReply> childReplies) {
        this.childReplies = childReplies;
    }

}
