package webprogramming.csc1106.Controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import webprogramming.csc1106.Entities.*;
import webprogramming.csc1106.Repositories.CategoryRepo;
import webprogramming.csc1106.Repositories.PostRepo;

import org.springframework.ui.Model;


@Controller
public class CommunityCategoryController {

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private PostRepo postRepo;

    // Method for returning views of general categories that are not within a group, such as Off-topic and Feedback
    @GetMapping("/community/general/{category_id}")
    public String getCategoryPosts(@PathVariable String category_id, Model model) {
        CommunityCategory category = categoryRepo.findById(Integer.parseInt(category_id)); // retrieve category object from db by name
        List<Post> posts = category.getPosts(); // get retrieved category's posts
        
        model.addAttribute("user_group", "general");
        model.addAttribute("category_name", category.getName()); // add category name to template model
        model.addAttribute("category_id", category_id); // used for new post later
        model.addAttribute("posts", posts); // add posts to template model

        return "Community/community-category";
    }

    // Method for returning views of categoreis within a group, such as Announcement, Student, Instructor
    @GetMapping("/community/{user_group}/{category_id}")
    public String getCategoryPosts(@PathVariable String user_group, @PathVariable String category_id, Model model) {
        CommunityCategory category = categoryRepo.findById(Integer.parseInt(category_id)); // retrieve category object from db by name
        List<Post> posts = category.getPosts(); // get retrieved category's posts
        
        model.addAttribute("user_group", user_group);
        model.addAttribute("category_name", category.getName()); // add category name to template model
        model.addAttribute("category_id", category_id); // used for new post later
        model.addAttribute("posts", posts); // add posts to template model

        return "Community/community-category";
    }

    @GetMapping("/community/announcements")
    public String getAnnouncements(Model model) {
        return "Community/community-announce";
    }

    @GetMapping("/community/students")
    public String getStudents(Model model) {
        List<Integer> categoryIds = Arrays.asList(3, 4, 5, 6, 7, 8);
        List<Post> posts = postRepo.findTop5ByCategoryIdInOrderByTimestampDesc(categoryIds);

        model.addAttribute("posts", posts);
        return "Community/community-students";
    }

    @GetMapping("/community/instructors")
    public String getInstructors(Model model) {
        List<Integer> categoryIds = Arrays.asList(9, 10);
        List<Post> posts = postRepo.findTop5ByCategoryIdInOrderByTimestampDesc(categoryIds);

        model.addAttribute("posts", posts);
        return "Community/community-instructors";
    }

    @GetMapping("/community/general")
    public String getGeneral(Model model) {
        return "redirect:/community";
    }
    
}
