package com.palci.CoachProgram.Controllers;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @GetMapping("/{clientId}")
    public String renderStats(@PathVariable long clientId, @AuthenticationPrincipal UserEntity userEntity, Model model) {
        ClientEntity clientEntity = clientRepository.findById(clientId).orElseThrow();
        ClientDTO clientDTO = clientService.getClientById(clientId);

        int allTrainings = trainingRepository.findAllByClientOrderByDateAsc(clientEntity).size();
        int doneTrainings = trainingRepository.findAllByClientAndDoneTrueOrderByDateAsc(clientEntity).size();






        model.addAttribute("client",clientDTO);



        return "pages/statistic/statistic";
    }
}
