package demo.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    // One syntax to implement a
    // GET method
    @GetMapping("/")
    public ModelAndView home()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("demo.html");
        return modelAndView;
    }
  
    // Another syntax to implement a
    // GET method
    @RequestMapping(
        method = { RequestMethod.GET },
        value = { "/home2" })
    public String info()
    {
        String str2
            = "<html><body><font color=\"Red\">"
              + "<h2>WELCOME AGAIN"
              + "</h2></font></body></html>";
        return str2;
    }
}