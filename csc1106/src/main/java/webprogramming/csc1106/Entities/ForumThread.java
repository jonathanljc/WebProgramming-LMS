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
public class ForumThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int threadID; // primary key

    // need to map to CourseForum(forumID)
    // private int forumID; // foreign key to CourseForum table

    @ManyToOne
    @JoinColumn(name = "forum_id")
    private CourseForum forum;

    @OneToMany(mappedBy = "thread")
    private List<ThreadReply> replies;

    private String posterName; // name of user that posted the thread
    private Date postDate; 
    private Time postTime;
    private String title;
    private String content;

    public List<ThreadReply> getReplies() {
        return replies;
    }

    public void setReplies(List<ThreadReply> replies) {
        this.replies = replies;
    }

    public int getThreadID() {
        return threadID;
    }

    // public void setThreadID(int threadID) {
    //     this.threadID = threadID;
    // }

    public CourseForum getForum() {
        return forum;
    }

    public void setForum(CourseForum forum) {
        this.forum = forum;
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
