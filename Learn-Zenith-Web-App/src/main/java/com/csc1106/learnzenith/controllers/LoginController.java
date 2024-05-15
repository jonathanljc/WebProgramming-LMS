package com.csc1106.learnzenith.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.csc1106.learnzenith.entities.User;

import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "pages-login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        // Handle login
        // For now, just print the username and password
        System.out.println("Username: " + user.getUserName());
        System.out.println("Password: " + user.getUserPassword());
        return "redirect:/dashboard";
    }
}