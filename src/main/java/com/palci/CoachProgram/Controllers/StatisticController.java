package com.palci.CoachProgram.Controllers;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.DTO.StatisticDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import com.palci.CoachProgram.Models.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.temporal.IsoFields;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/clients/statistic")
public class StatisticController {
    @Autowired
    ClientService clientService;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    StatisticService statisticService;
    @Autowired
    WeightRepository weightRepository;


    @GetMapping("/{clientId}")
    public String renderStats(@PathVariable long clientId, @AuthenticationPrincipal UserEntity userEntity, Model model) {


        ClientEntity clientEntity = clientRepository.findById(clientId).orElseThrow();

        if (clientEntity.getOwner().getUserId() != userEntity.getUserId()){
            throw new AccessDeniedException("Not allowed");
        }


        ClientDTO clientDTO = clientService.getClientById(clientId);
        List<WeightEntity> weightHistory = weightRepository.findAllByClientOrderByDateAsc(clientEntity);
        StatisticDTO statisticDTO = statisticService.getStatistic(clientEntity);

        model.addAttribute("statistic",statisticDTO);
        model.addAttribute("client",clientDTO);

        // Chart.js - weights
        double originalWeight = clientEntity.getOriginalWeight();
        model.addAttribute("originalWeight",originalWeight);
        List<String> dates = weightHistory.stream()
                .map(entry->entry.getDate().toString())
                .toList();
        List<Double> weights = weightHistory.stream()
                .map(WeightEntity::getNewWeight)
                .toList();

        model.addAttribute("data",weights);
        model.addAttribute("labels",dates);
        // End of region for char.js

        // Chart.js - trainings in one week
        List<TrainingEntity> doneTrainings = trainingRepository.findAllByClientAndDoneTrue(clientEntity);
        Map<String,Long> doneTrainingsByWeek = doneTrainings.stream()
                .collect(Collectors.groupingBy(
                        training -> {
                            int week = training.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                            int year = training.getDate().getYear();
                            return year + "-W" + week;
                        },
                        Collectors.counting()
                ));
        model.addAttribute("doneTrainingsByWeek",doneTrainingsByWeek);

        return "pages/statistic/statistic";
    }
}
