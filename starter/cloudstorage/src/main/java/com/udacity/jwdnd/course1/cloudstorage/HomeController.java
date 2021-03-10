package com.udacity.jwdnd.course1.cloudstorage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String homeView() {
        return "home";
    }
}
