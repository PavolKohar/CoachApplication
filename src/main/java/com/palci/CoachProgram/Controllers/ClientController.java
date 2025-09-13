package com.palci.CoachProgram.Controllers;


import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Enums.Program;
import com.palci.CoachProgram.Models.Services.ClientService;
import com.palci.CoachProgram.Models.Services.TrainingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Secured({"ROLE_ADMIN", "ROLE_USER"})
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    ClientService clientService;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    TrainingService trainingService;

    //Badges
    @ModelAttribute
    public void createBadgesAttributes(@AuthenticationPrincipal UserEntity userEntity ,Model model){
        model.addAttribute("allClients", clientService.getDTOClientsForUser(userEntity).size());
        model.addAttribute("male",clientService.getClientsBySex(userEntity,"man").size());
        model.addAttribute("female",clientService.getClientsBySex(userEntity,"woman").size());
        model.addAttribute("weightLoss",clientService.getClientByProgram(userEntity,Program.WEIGHT_LOSS).size());
        model.addAttribute("muscleGain",clientService.getClientByProgram(userEntity, Program.MUSCLE_GAIN).size());
        model.addAttribute("maintain",clientService.getClientByProgram(userEntity,Program.MAINTAIN).size());
        model.addAttribute("active",clientService.getClientsByActiveStatus(userEntity,true).size());
        model.addAttribute("passive",clientService.getClientsByActiveStatus(userEntity,false).size());
    }
    // End of badges region

    @GetMapping
    public String renderBasicIndex(@AuthenticationPrincipal UserEntity userEntity, Model model){
        String username = userEntity.getUsername();
        model.addAttribute("name",username);

        List<ClientDTO> clients = clientService.getDTOClientsForUser(userEntity);

        model.addAttribute("clients",clients);
        List<TrainingEntity> todayTrainings = trainingService.getTodayTrainings(userEntity);
        model.addAttribute("todayTrainings",todayTrainings);
        return "pages/clients/index";
    }

    @GetMapping("{filter}")
    public String renderProfileWithFilter(@AuthenticationPrincipal UserEntity userEntity, @PathVariable String filter, Model model){
        String username = userEntity.getUsername();
        model.addAttribute("name",username);

        List<ClientDTO> clients;
        switch (filter){
            case "male"-> clients = clientService.getClientsBySex(userEntity,"man");
            case "female" -> clients = clientService.getClientsBySex(userEntity,"woman");
            case "weight-loss" -> clients = clientService.getClientByProgram(userEntity, Program.WEIGHT_LOSS);
            case "muscle-gain" -> clients = clientService.getClientByProgram(userEntity,Program.MUSCLE_GAIN);
            case "maintain" -> clients = clientService.getClientByProgram(userEntity,Program.MAINTAIN);
            case "active" -> clients = clientService.getClientsByActiveStatus(userEntity,true);
            case "passive" -> clients = clientService.getClientsByActiveStatus(userEntity,false);
            default -> clients = clientService.getDTOClientsForUser(userEntity);
        }

        List<TrainingEntity> todayTrainings = trainingService.getTodayTrainings(userEntity);
        model.addAttribute("todayTrainings",todayTrainings);
        model.addAttribute("clients",clients);


        return "pages/clients/index";
    }

    @GetMapping("/create")
    public String renderCreateForm(@ModelAttribute ClientDTO clientDTO){
        return "pages/clients/create";
    }


    @PostMapping("/create")
    public String addNewClient(@Valid @ModelAttribute ClientDTO clientDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal UserEntity userEntity){
        String email = userEntity.getEmail();

        if (bindingResult.hasErrors()){
            return renderCreateForm(clientDTO);
        }



        clientService.addClient(clientDTO,email);

        redirectAttributes.addFlashAttribute("success","Client created");

        return "redirect:/clients";

    }

    @GetMapping("/full-edit/{clientId}")
    public String renderFullEditForm(@PathVariable long clientId,@AuthenticationPrincipal UserEntity userEntity, Model model){
        ClientDTO clientDTO = clientService.getClientById(clientId);

        if (clientDTO.getOwnerId() != userEntity.getUserId()){
            throw new AccessDeniedException("You are not allowed to edit this client");
        }

        model.addAttribute("clientDTO",clientDTO);
        return "pages/clients/fullEdit";

    }

    // Editing client
    @PostMapping("/full-edit/{clientId}")
    public String updateClient(@PathVariable long clientId,@ModelAttribute("clientDTO")ClientDTO clientDTO){
        clientService.update(clientDTO,clientId);

        return "redirect:/clients/detail/{clientId}";
    }





}
