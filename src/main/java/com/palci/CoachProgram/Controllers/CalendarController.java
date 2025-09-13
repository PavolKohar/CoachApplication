package com.palci.CoachProgram.Controllers;


import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/calendar")
public class CalendarController {

    @GetMapping
    public String renderCalendar(){
        return "pages/calendar/calendar";
    }
}
