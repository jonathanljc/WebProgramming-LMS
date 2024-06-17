package webprogramming.csc1106.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpStatus;


import webprogramming.csc1106.Entities.*;
import webprogramming.csc1106.Repositories.PostRepo;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CommunityController {

    @Autowired
    private PostRepo postRepo;
    
    @GetMapping("/community")
    public String getCommunityHome(@RequestParam(defaultValue = "1") int page, Model model) {
        List<Post> posts = postRepo.findTop5ByOrderByTimestampDesc();

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);

        return "Community/community-home";
    }
    
    @GetMapping("/community/search")
    public String getSearchResults(@RequestParam("key") String key, @RequestParam(defaultValue = "1") int page, Model model) {
        Page<Post> queriedPosts = postRepo.findAllByTitleContainingOrContentContainingOrderByTimestampDesc(key, key, PageRequest.of(page - 1, 5));
        
        model.addAttribute("posts", queriedPosts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("search_term", key);
        model.addAttribute("totalPage", queriedPosts.getTotalPages());
        return "Community/community-search";
    }

    @SuppressWarnings("null")
    @PostMapping("/community-get-post-count")
    public ResponseEntity<List> getPostsCount(){
        try{

            List<Object[]> categoryCounts = postRepo.findCategoryCounts();
            
            Long totalAnnouncements = 0L;
            Long totalStudents = 0L;
            Long totalInstructors = 0L;
            
            for (int i = 0; i < categoryCounts.size(); i++) {
                if("announcements".equals(categoryCounts.get(i)[1])){
                    totalAnnouncements += (Long) categoryCounts.get(i)[2];
                } else if("students".equals(categoryCounts.get(i)[1])){
                    totalStudents += (Long) categoryCounts.get(i)[2];
                } else if("instructors".equals(categoryCounts.get(i)[1])){
                    totalInstructors += (Long) categoryCounts.get(i)[2];
                }
            }
            
            return new ResponseEntity<>(List.of(totalAnnouncements, totalStudents, totalInstructors), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
