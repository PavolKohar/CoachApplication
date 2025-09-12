package com.palci.CoachProgram.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {


    @GetMapping
    public String renderHomePage(){
        return "pages/home/index";
    }


    @GetMapping("about-us")
    public String renderAboutUs(){
        return "pages/home/about";
    }
}
