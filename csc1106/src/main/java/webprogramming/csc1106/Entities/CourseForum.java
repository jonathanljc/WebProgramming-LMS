package webprogramming.csc1106.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class CourseForum {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int forumID; // primary key

    @OneToMany(mappedBy = "forum")
    private List<ForumThread> threads;
    
    private String description; // for forum description to set rules and stuff

    // need to map to courses(courseID), tbc)
    // private int courseID; // foreign key to Courses table
    
    // public int getCourseID() {
    //     return courseID;
    // }

    // public void setCourseID(int courseID) {
    //     this.courseID = courseID;
    // }

    public int getForumID() {
        return forumID;
    }

    // public void setForumID(int numOfThreads) {
    //     this.forumID = numOfThreads;
    // }

    public List<ForumThread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<ForumThread> threads) {
        this.threads = threads;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
