package com.palci.CoachProgram.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clients")
public class ClientController {


    @GetMapping
    public String renderBasicIndex(){
        return "pages/clients/index";
    }

}
