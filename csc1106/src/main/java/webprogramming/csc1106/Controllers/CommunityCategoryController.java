package webprogramming.csc1106.Controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String getCategoryPosts(@RequestParam(defaultValue = "1") int page, @PathVariable String category_id, Model model) {
        Optional<CommunityCategory> category = categoryRepo.findById(Integer.parseInt(category_id)); // retrieve category object from db by name

        if (!category.isPresent()) {
            System.err.println("Unrecognised category ID");
            return "redirect:/community";
        }

        Page<Post> posts = postRepo.findAllByCategoryOrderByTimestampDesc(category.get(), PageRequest.of(page - 1, 15)); // get retrieved category's posts
        
        model.addAttribute("currentNum", page);
        model.addAttribute("user_group", "general");
        model.addAttribute("category_name", category.get().getName()); // add category name to template model
        model.addAttribute("category_id", category.get().getId()); // used for new post later
        model.addAttribute("posts", posts); // add posts to template model
        model.addAttribute("totalPage", posts.getTotalPages());

        return "Community/community-category";
    }

    // Method for returning views of categories within a group, such as Announcement, Student, Instructor
    @GetMapping("/community/{user_group}/{category_id}")
    public String getCategoryPosts(@RequestParam(defaultValue = "1") int page, @PathVariable String user_group, @PathVariable String category_id, Model model) {
        Optional<CommunityCategory> category = categoryRepo.findById(Integer.parseInt(category_id)); // retrieve category object from db by name

        if (!category.isPresent()) {
            System.err.println("Unrecognised category ID");
            return "redirect:/community";
        }

        Page<Post> posts = postRepo.findAllByCategoryOrderByTimestampDesc(category.get(), PageRequest.of(page - 1, 15)); // get retrieved category's posts
        
        model.addAttribute("currentNum", page);
        model.addAttribute("user_group", user_group);
        model.addAttribute("category_name", category.get().getName()); // add category name to template model
        model.addAttribute("category_id", category_id); // used for new post later
        model.addAttribute("posts", posts.getContent()); // add posts to template model
        model.addAttribute("totalPage", posts.getTotalPages());

        return "Community/community-category";
    }

    // Mapping for announcements page
    @GetMapping("/community/announcements")
    public String getAnnouncements(Model model) {
        List<Integer> categoryIds = Arrays.asList(1, 2);
        List<Post> posts = postRepo.findTop5ByCategoryIdInOrderByTimestampDesc(categoryIds);

        model.addAttribute("posts", posts);
        return "Community/community-announce";
    }

    // Mapping for students page
    @GetMapping("/community/students")
    public String getStudents(Model model) {
        // get all the categories under student group
        List<CommunityCategory> categories = categoryRepo.findByGroup("students");

        model.addAttribute("categories", categories);
        return "Community/community-students";
    }

    // Mapping for instructors page
    @GetMapping("/community/instructors")
    public String getInstructors(Model model, @CookieValue(value="lrnznth_User_ID", required=false) String userID) {
        if(userID == null) {
            return "redirect:/login";
        }
        
        return "Community/community-instructors";
    }
    
    // Mapping to redirect to community home page from general path
    // so when users clicks "General" breadcrumb link, returns to community homepage
    @GetMapping("/community/general")
    public String getGeneral(Model model) {
        return "redirect:/community";
    }
}
