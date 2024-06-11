package webprogramming.csc1106.Controllers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import webprogramming.csc1106.Entities.*;
import webprogramming.csc1106.Models.LikesID;
import webprogramming.csc1106.Repositories.*;

@Controller
public class CommunityPostController {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private LikesRepo likesRepo;

    @GetMapping("/community/{user_group}/{category_id}/{post_id}")
    public String getPost(@PathVariable String category_id, @PathVariable String post_id, @PathVariable String user_group, @CookieValue("lrnznth_User_Name") String username, Model model) {
        // retrieve models
        Long post_ID = Long.parseLong(post_id);
        Post post = postRepo.findByPostID(post_ID);
        String category_name = post.getCategory().getName();
        List<PostAttachments> attachments = post.getAttachments();
        User user = userRepo.findByUserName(username);
        LikesID likesID = new LikesID(user.getUserID(), post.getPostID());
        Optional<Likes> like = likesRepo.findById(likesID);

        // extract urls from attachments list
        ArrayList<String> urls = new ArrayList<String>();
        if (!attachments.isEmpty()) {
            for (PostAttachments attachment : attachments) {
                urls.add(attachment.getURI());
            }
            model.addAttribute("attachment_urls", urls);
        }
        else {
            model.addAttribute("attach_urls", new String[0]); // make empty arraylist if no attachments retrieved
        }

        // add attributes to thymeleaf model
        model.addAttribute("post", post);
        model.addAttribute("category_name", category_name);
        model.addAttribute("user_group", user_group);
        model.addAttribute("newComment", new Comment());
        model.addAttribute("comments", post.getComments().reversed());
        if (like.isPresent()) {
            model.addAttribute("liked", true);
        }
        else {
            model.addAttribute("liked", false);
        }

        return "Community/post";
    }

    @PostMapping("/community/{user_group}/{category_id}/{post_id}/add-comment")
    public String postComment(@ModelAttribute Comment newComment, @PathVariable String user_group, @PathVariable String category_id, @PathVariable String post_id, @CookieValue("lrnznth_User_Name") String username) {
        java.util.Date date = new java.util.Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        newComment.setTimestamp(timestamp);
        newComment.setCommenterName(username);

        Post post = postRepo.findByPostID(Long.parseLong(post_id));
        // post.getComments().add(newComment);
        newComment.setPost(post);

        // postRepo.save(post);
        commentRepo.save(newComment);
        
        return "redirect:/community/{user_group}/{category_id}/{post_id}";
    }
    
    @PostMapping("/community/{user_group}/{category_id}/{post_id}/like")
    public ResponseEntity<String> likePost(@PathVariable String user_group, @PathVariable String category_id, @PathVariable String post_id, @RequestParam("username") String username) {
        try{
            // retrieve post object and increment like count
            Post likedPost = postRepo.findByPostID(Long.parseLong(post_id));
            likedPost.setLikes(likedPost.getLikes() + 1);
            
            // retrieve user object, create new like object and save to repo
            User user = userRepo.findByUserName(username);
            LikesID likesID = new LikesID(user.getUserID(), likedPost.getPostID());
            Likes newLike = new Likes(likesID, user, likedPost);
            
            likesRepo.save(newLike);
            postRepo.save(likedPost);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/community/{user_group}/{category_id}/{post_id}/unlike")
    public ResponseEntity<String> unlikePost(@PathVariable String user_group, @PathVariable String category_id, @PathVariable String post_id, @RequestParam("username") String username) {
        try{
            // retrieve post object and decrement like count
            Post unlikedPost = postRepo.findByPostID(Long.parseLong(post_id));
            unlikedPost.setLikes(unlikedPost.getLikes() - 1);
            postRepo.save(unlikedPost);

            // retrieve user object, create likesID object and delete from repo by likesID(composite key)
            User user = userRepo.findByUserName(username);
            LikesID likeID = new LikesID(user.getUserID(), unlikedPost.getPostID());
            likesRepo.deleteById(likeID);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}   
