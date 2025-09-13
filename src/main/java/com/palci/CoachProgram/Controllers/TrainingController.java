package com.palci.CoachProgram.Controllers;

import com.palci.CoachProgram.Data.Entities.*;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingPlanRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.PlanDTO;
import com.palci.CoachProgram.Models.DTO.TrainingDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import com.palci.CoachProgram.Models.Services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/training")
public class TrainingController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    TrainingService trainingService;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    WeightRepository weightRepository;
    @Autowired
    TrainingPlanRepository planRepository;


    @GetMapping
    public String renderTrainingPlanner(@ModelAttribute PlanDTO planDTO, @AuthenticationPrincipal UserEntity userEntity, Model model) {
        List<ClientEntity> clients = clientRepository.findAllByOwner(userEntity);
        model.addAttribute("clients", clients);


        return "pages/training/createForm";
    }

    @GetMapping("/{clientId}")
    public String renderTrainingPlanerForClient(@ModelAttribute PlanDTO planDTO, @AuthenticationPrincipal UserEntity userEntity, @PathVariable long clientId, Model model) {
        ClientEntity client = clientService.getByIdOrThrow(clientId);

        model.addAttribute("clients", client);


        return "pages/training/createFormForClient";
    }

    @PostMapping("/create")
    public String createTrainingPlan(@ModelAttribute PlanDTO planDTO, @AuthenticationPrincipal UserEntity userEntity) {
        ClientEntity client = clientService.getByIdOrThrow(planDTO.getClientId());
        trainingService.createPlan(client, userEntity, planDTO.getWeeks(), planDTO.getWorkoutsPerWeek(), planDTO.getLocalDate(), planDTO.getTime());


        return "redirect:/clients/detail/" + client.getClientId();
    }

    @GetMapping("/done/{trainingId}/{clientId}")
    public String trainingDone(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long trainingId,@PathVariable long clientId){
        TrainingEntity trainingEntity = trainingRepository.findById(trainingId).orElseThrow();
        ClientEntity clientEntity = clientService.getByIdOrThrow(clientId);
        TrainingPlanEntity plan = trainingEntity.getPlan();

        if (trainingEntity.getClient().getClientId() != clientEntity.getClientId() || clientEntity.getOwner().getUserId() != userEntity.getUserId()){
            throw new AccessDeniedException("You have not right to do this action.");
        }else {
            if (trainingEntity.getDate().isAfter(LocalDate.now())){
                trainingEntity.setDate(LocalDate.now());
            }

            trainingEntity.setDone(true);
            trainingRepository.save(trainingEntity);
            plan.updateProgress();
            planRepository.save(plan);
        }

        return "redirect:/clients/detail/{clientId}";
    }

    @GetMapping("/client/done/{trainingId}/{clientId}")
    public String trainingDoneByUser(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long trainingId,@PathVariable long clientId){
        TrainingEntity trainingEntity = trainingRepository.findById(trainingId).orElseThrow();
        ClientEntity clientEntity = clientService.getByIdOrThrow(clientId);
        TrainingPlanEntity plan = trainingEntity.getPlan();

        if (trainingEntity.getClient().getClientId() != clientEntity.getClientId() || clientEntity.getOwner().getUserId() != userEntity.getUserId()){
            throw new AccessDeniedException("You have not right to do this action.");
        }else {
            if (trainingEntity.getDate().isAfter(LocalDate.now())){
                trainingEntity.setDate(LocalDate.now());
            }

            trainingEntity.setDone(true);
            trainingRepository.save(trainingEntity);
            plan.updateProgress();
            planRepository.save(plan);
        }

        return "redirect:/clients";
    }

    @GetMapping("/detail/{trainingId}/{clientId}")
    public String renderDetailTrainingForm(@AuthenticationPrincipal UserEntity userEntity,
                                           @PathVariable long trainingId,
                                           @PathVariable long clientId,
                                           Model model) {

        TrainingEntity trainingEntity = trainingRepository.findById(trainingId).orElseThrow();
        ClientEntity clientEntity = clientService.getByIdOrThrow(clientId);

        if (trainingEntity.getClient().getClientId() != clientEntity.getClientId()
                || clientEntity.getOwner().getUserId() != userEntity.getUserId()) {
            throw new AccessDeniedException("You have not right to do this action.");
        }

        TrainingDTO trainingDTO = trainingService.toDto(trainingEntity);

        model.addAttribute("trainingDTO", trainingDTO);
        return "pages/training/detail";
    }

    @PostMapping("/detail/{trainingId}")
    public String saveUpdateTraining(@PathVariable long trainingId,@ModelAttribute TrainingDTO trainingDTO){
        trainingService.updateTraining(trainingId,trainingDTO); // TODO Take client Detail from trainingId and redirect after edit


        return "redirect:/clients";
    }
}
