package webprogramming.csc1106.Controllers;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import webprogramming.csc1106.Entities.ForumThread;
import webprogramming.csc1106.Entities.ThreadReply;
import webprogramming.csc1106.Repositories.ForumThreadRepo;
import webprogramming.csc1106.Repositories.ThreadReplyRepo;

@Controller
public class ThreadController {
    
    @Autowired
    private ForumThreadRepo forumThreadRepo;

    @Autowired
    private ThreadReplyRepo threadReplyRepo;

    @GetMapping("/forum/{id}")
    public String openThread(@PathVariable String id, Model model) {
        int threadId = Integer.parseInt(id); // Convert String to int

        ForumThread threadSelected = forumThreadRepo.findByThreadID(threadId);

        // Optional<ForumThread> thread = forumThreadRepo.findById(threadId); // Find thread by id
        // ArrayList<ForumThread> threadSelected = new ArrayList<>();

        // thread.ifPresent(t -> threadSelected.add(t));

        // model.addAttribute("threadSelected", threadSelected.toArray());

        model.addAttribute("threadSelected", threadSelected);

        List<ThreadReply> threadReplies = threadSelected.getReplies(); // Find replies by id
        ArrayList<ThreadReply> replies = new ArrayList<>();
        ArrayList<ThreadReply> replyReplies = new ArrayList<>();
        for (ThreadReply r : threadReplies) {
            if (r.getParent() == null){
                replies.add(r);
            }else{
                replyReplies.add(r);
            }
        }

        model.addAttribute("replies", replies.toArray());
        model.addAttribute("replyReplies", replyReplies.toArray());
        // System.out.println(replies.toArray());
        model.addAttribute("newReply", new ThreadReply());

        return "thread";
    }

    @PostMapping("/add-reply/{commentID}/{threadID}")
    public String addReply(@ModelAttribute ThreadReply newReply, Model model, @PathVariable String commentID, @PathVariable String threadID){
        java.util.Date date = new java.util.Date();
        Date sqlDate = new Date(date.getTime());
        Time sqlTime = new Time(date.getTime());
        newReply.setReplyDate(sqlDate);
        newReply.setReplyTime(sqlTime);
        newReply.setResponderName("Example name"); // placeholder
        newReply.setParent(threadReplyRepo.findByReplyID(Integer.parseInt(commentID)));
        newReply.setThread(forumThreadRepo.findByThreadID(Integer.parseInt(threadID)));

        threadReplyRepo.save(newReply);

        // need to change so it actually redirects to the newly created thread's dedicated page
        return "redirect:/forum/{threadID}"; 
    }

}
