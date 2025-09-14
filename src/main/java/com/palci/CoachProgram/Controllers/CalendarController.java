package com.palci.CoachProgram.Controllers;


import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Models.Services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    TrainingService trainingService;
    @Autowired
    TrainingRepository trainingRepository;

    @GetMapping
    public String renderCalendar(@AuthenticationPrincipal UserEntity userEntity, Model model){
        String username = userEntity.getUsername();
        model.addAttribute("name",username);

        List<TrainingEntity> todayTrainings = trainingService.getTodayTrainings(userEntity);
        model.addAttribute("today",todayTrainings);
        List<TrainingEntity> thisWeekTrainings = trainingService.getThisWeekTrainings(userEntity);
        model.addAttribute("thisWeek",thisWeekTrainings);
        List<TrainingEntity> nextWeekTrainings = trainingService.getNextWeekTrainings(userEntity);
        model.addAttribute("nextWeek",nextWeekTrainings);
        List<TrainingEntity> thisMonthTrainings = trainingService.getThisMonthTrainings(userEntity);
        model.addAttribute("thisMonth",thisMonthTrainings);
        List<TrainingEntity> allTrainings = trainingRepository.findAllByUserOrderByDateAsc(userEntity);
        model.addAttribute("allTrainings",allTrainings);


        return "pages/calendar/calendar";
    }
}
