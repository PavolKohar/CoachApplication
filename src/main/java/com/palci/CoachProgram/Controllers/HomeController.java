package com.palci.CoachProgram.Controllers;

import com.palci.CoachProgram.Models.DTO.UserDTO;
import com.palci.CoachProgram.Models.Exceptions.DuplicateEmailException;
import com.palci.CoachProgram.Models.Exceptions.PasswordsDoNotEqualException;
import com.palci.CoachProgram.Models.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    UserService userService;


    @GetMapping
    public String renderHomePage(){
        return "pages/home/index";
    }


    @GetMapping("about-us")
    public String renderAboutUs(){
        return "pages/home/about";
    }

    @GetMapping("contact")
    public String renderContact(){
        return "pages/home/contact";
    }

    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute UserDTO userDTO){
        return "pages/home/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDTO userDTO,
                           BindingResult result,
                           RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return renderRegisterForm(userDTO);
        }

        try {
            userService.create(userDTO,false);
        }catch (DuplicateEmailException e){
            result.rejectValue("email","error","Email already in use");
            return "pages/home/register";
        }catch (PasswordsDoNotEqualException e){
            result.rejectValue("password","error","Passwords do not match");
            result.rejectValue("confirmPassword","error","Passwords do not match");
            return "pages/home/register";
        }


        redirectAttributes.addFlashAttribute("success","User was register");


        return "pages/home/login";
    }

    @GetMapping("login")
    public String renderLoginForm(){
        return "pages/home/login";
    }
}
