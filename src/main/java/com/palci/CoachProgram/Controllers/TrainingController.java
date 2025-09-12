package com.palci.CoachProgram.Controllers;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Models.DTO.PlanDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/training")
public class TrainingController {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;


    @GetMapping
    public String renderTrainingPlanner(@ModelAttribute PlanDTO planDTO, @AuthenticationPrincipal UserEntity userEntity, Model model){
        List<ClientEntity> clients = clientRepository.findAllByOwner(userEntity);
        model.addAttribute("clients",clients);


        return "pages/training/createForm";
    }

    @GetMapping("/{clientId}")
    public String renderTrainingPlanerForClient(@ModelAttribute PlanDTO planDTO, @AuthenticationPrincipal UserEntity userEntity, @PathVariable long clientId, Model model){
        ClientEntity client = clientService.getByIdOrThrow(clientId);

        model.addAttribute("clients",client);


        return "pages/training/createFormForClient";
    }
}
