package com.csc1106.learnzenith.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MarketPlaceController {

    @GetMapping("/MarketPlace")
    public String showMarketPlacePage(Model model) {
        model.addAttribute("title!", "Market Place");
        model.addAttribute("searchQuery", "");
        model.addAttribute("categories", List.of("Category 1", "Category 2", "Category 3"));
        return "MarketPlace";  // This should be the name of your HTML file without the .html extension
    }
}