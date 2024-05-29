package webprogramming.csc1106.Controllers;

import java.sql.Date;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import webprogramming.csc1106.Entities.CourseForum;
import webprogramming.csc1106.Entities.ForumThread;
import webprogramming.csc1106.Repositories.CourseForumRepo;
import webprogramming.csc1106.Repositories.ForumThreadRepo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostNewThreadController {

    @Autowired
    private ForumThreadRepo forumThreadRepo;
    @Autowired
    private CourseForumRepo courseForumRepo;

    @GetMapping("/post-thread")
    public String getNewThreadForm(@RequestParam(defaultValue = "1") int forumID, Model model) {
        model.addAttribute("newThread", new ForumThread());
        model.addAttribute("forumID", forumID);
        return "post-thread"; 
    }

    @PostMapping("/post-thread/{forumID}")
    public String postNewThread(@ModelAttribute ForumThread newThread, @PathVariable String forumID, Model model) {
        java.util.Date date = new java.util.Date();
        Date sqlDate = new Date(date.getTime());
        Time sqlTime = new Time(date.getTime());
        CourseForum forum = courseForumRepo.findByForumID(Integer.parseInt(forumID));
        newThread.setForum(forum);
        newThread.setPostDate(sqlDate);
        newThread.setPostTime(sqlTime);
        newThread.setPosterName("Example name"); // placeholder
        
        forumThreadRepo.save(newThread);
        forum.getThreads().add(newThread);
        courseForumRepo.save(forum);
        
        // need to change so it actually redirects to the newly created thread's dedicated page
        return "redirect:forum"; 
    }
    
}
