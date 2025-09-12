package com.palci.CoachProgram.Controllers;


import com.palci.CoachProgram.Data.Entities.UserEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/clients")
public class ClientController {


    @GetMapping
    public String renderBasicIndex(@AuthenticationPrincipal UserEntity userEntity, Model model){
        String username = userEntity.getUsername();
        model.addAttribute("name",username);
        return "pages/clients/index";
    }

}
