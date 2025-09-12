package com.palci.CoachProgram.Controllers;


import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients/detail")
@Secured({"ROLE_ADMIN", "ROLE_USER"})
public class ClientDetailController {
    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    WeightRepository weightRepository;
    @Autowired
    TrainingRepository trainingRepository;

    @GetMapping("/{clientId}")
    public String renderDetail(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long clientId, Model model){
        ClientDTO clientDTO = clientService.getClientById(clientId);
        ClientEntity entity = clientRepository.findById(clientId).orElseThrow();
        List<WeightEntity> weightHistory = weightRepository.findAllByClientOrderByDateAsc(entity);
        List<WeightEntity> lastFiveWeightHistory = weightRepository.findTop5ByClientOrderByDateDesc(entity);

        if (clientDTO.getOwnerId() != userEntity.getUserId()){
            throw new AccessDeniedException("You are not allowed to edit this client");
        }

        // Adding attributes
        model.addAttribute("clientDTO",clientDTO);
        model.addAttribute("age",clientDTO.getAge());
        model.addAttribute("progress",clientService.giveProgress(clientId));
        model.addAttribute("historyFive",lastFiveWeightHistory);
        model.addAttribute("history",weightHistory);


        List<TrainingEntity> trainingEntities = trainingRepository.findAllByClientOrderByDateAsc(entity);
        trainingEntities = trainingEntities.stream().filter(t-> !t.isDone()).toList();
        model.addAttribute("trainings",trainingEntities);

        // Generating values for chart.js
        List<String> dates = weightHistory.stream()
                .map(entry->entry.getDate().toString())
                .toList();
        List<Double> weights = weightHistory.stream()
                .map(WeightEntity::getNewWeight)
                .toList();

        model.addAttribute("data",weights);
        model.addAttribute("labels",dates);




        return "pages/clients/detail";
    }


    @PostMapping("/{clientId}")
    public String updateCurrentWeight(@PathVariable long clientId,@ModelAttribute("clientDTO")ClientDTO clientDTO){

        clientService.updateCurrentWeight(clientId,clientDTO.getCurrentWeight());

        return "redirect:/clients/detail/{clientId}";
    }

}
