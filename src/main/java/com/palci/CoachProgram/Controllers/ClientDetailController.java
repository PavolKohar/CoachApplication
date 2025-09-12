package com.palci.CoachProgram.Controllers;


import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/{clientId}")
    public String renderDetail(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long clientId, Model model){
        ClientDTO clientDTO = clientService.getClientById(clientId);
        ClientEntity entity = clientRepository.findById(clientId).orElseThrow();

        List<WeightEntity> weightHistory = weightRepository.findAllByClientOrderByDateAsc(entity);

        if (clientDTO.getOwnerId() != userEntity.getUserId()){
            throw new AccessDeniedException("You are not allowed to edit this client");
        }

        // Adding attributes
        model.addAttribute("clientDTO",clientDTO);
        model.addAttribute("age",clientDTO.getAge());
        model.addAttribute("progress",clientService.giveProgress(clientId));




        return "pages/clients/detail";
    }

}
