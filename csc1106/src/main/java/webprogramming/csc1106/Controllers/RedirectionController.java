package webprogramming.csc1106.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class RedirectionController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/index.html")
    public String indexPage() {
        return "index";
    }
    
    @GetMapping("/blank")
    public String blank() {
        return "Extra/blank";
    }

    @GetMapping("/contact")
    public String contact() {
        return "User/contact";
    }

    @GetMapping("userprofile")
    public String userProfile() {
        return "User/userprofile";
    }
    
    @GetMapping("/refreshsidebar")
    public String refreshSidebar() {
        return "Extra/blank2";
    }

    @GetMapping("/logout")
    public String logout() {
        return "User/logout";
    }

}