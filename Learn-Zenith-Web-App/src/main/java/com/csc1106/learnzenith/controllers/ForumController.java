package com.csc1106.learnzenith.controllers;
import java.sql.Date;
import java.sql.Time;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.csc1106.learnzenith.entities.*;

import org.springframework.ui.Model;;

@Controller
public class ForumController {
    
    @GetMapping("/forum")
    public String forum(Model model) {
        Date date = new Date(100);
        Time time = new Time(1000);
        ForumThread thread1 = new ForumThread(1, 1, "Poster Name", date, time, 0, "Example title", "Example content");
        ForumThread thread2 = new ForumThread(2, 1, "Poster Name2", date, time, 0, "Example title again", "Example content again");
        model.addAttribute("thread1", thread1);
        model.addAttribute("thread2", thread2);
        return "forum"; 
    }
}
