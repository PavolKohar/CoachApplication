package com.palci.CoachProgram.Controllers;


import com.palci.CoachProgram.Data.Entities.*;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingPlanRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import com.palci.CoachProgram.Models.Services.TrainingService;
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
    @Autowired
    TrainingPlanRepository planRepository;


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

        List<TrainingEntity> nextFiveTrainings = trainingRepository.findTop5ByClientAndDoneFalseOrderByDateAsc(entity);
        model.addAttribute("fiveNextTrainings",nextFiveTrainings);

        // Generating values for chart.js - Weight history
        double originalWeight = entity.getOriginalWeight();
        model.addAttribute("originalWeight",originalWeight);
                List<String> dates = weightHistory.stream()
                .map(entry->entry.getDate().toString())
                .toList();
        List<Double> weights = weightHistory.stream()
                .map(WeightEntity::getNewWeight)
                .toList();

        model.addAttribute("data",weights);
        model.addAttribute("labels",dates);
        // End of region for char.jj

        // Region for training plan for clients
        List<TrainingPlanEntity> planEntities = planRepository.findAllByClientOrderByStartDateAsc(entity);
        model.addAttribute("plans",planEntities);


        return "pages/clients/detail";
    }


    @PostMapping("/{clientId}")
    public String updateCurrentWeight(@PathVariable long clientId,@ModelAttribute("clientDTO")ClientDTO clientDTO){

        clientService.updateCurrentWeight(clientId,clientDTO.getCurrentWeight());

        return "redirect:/clients/detail/{clientId}";
    }


    // Removing entry from table
    @GetMapping("/remove-entry/{clientId}/{changeId}")
    public String removeEntry(@PathVariable long changeId,@PathVariable long clientId,@AuthenticationPrincipal UserEntity userEntity){
        WeightEntity weightEntity = weightRepository.findById(changeId).orElseThrow();
        ClientEntity clientEntity = clientRepository.findById(clientId).orElseThrow();

        // Basic authorization
        if (weightEntity.getClient().getClientId() == clientId && clientEntity.getOwner().getUserId() == userEntity.getUserId()){
            weightRepository.deleteById(changeId);
        }else {
            throw new AccessDeniedException("You are not allowed to delete this record.");

        }

        return "redirect:/clients/detail/{clientId}";
    }

    // Deleting client from client repository
    @GetMapping("/delete/{clientId}")
    public String deleteClient(@PathVariable long clientId,@AuthenticationPrincipal UserEntity userEntity){
        ClientDTO clientDTO = clientService.getClientById(clientId);

        if (clientDTO.getOwnerId() != userEntity.getUserId()){
            throw new AccessDeniedException("You are not allowed to edit this client");
        }

        clientService.deleteById(clientId);

        return "redirect:/clients";

    }

    @GetMapping("/toggle/{clientId}")
    public String changeActiveStatus(@PathVariable long clientId){
        clientService.toggleActive(clientId);

        return "redirect:/clients/detail/{clientId}";
    }

    @GetMapping("/remove-plan/{clientId}/{planId}")
    public String removeTrainingPlan(@PathVariable long planId,@PathVariable long clientId,@AuthenticationPrincipal UserEntity userEntity){

        TrainingPlanEntity planEntity = planRepository.findById(planId).orElseThrow();
        ClientEntity clientEntity = clientRepository.findById(clientId).orElseThrow();

        // Basic authorization
        if (planEntity.getClient().getClientId() == clientId && clientEntity.getOwner().getUserId() == userEntity.getUserId()){
            planRepository.deleteById(planId);
        }else {
            throw new AccessDeniedException("You are not allowed to delete this record.");

        }

        return "redirect:/clients/detail/{clientId}";
    }

}
