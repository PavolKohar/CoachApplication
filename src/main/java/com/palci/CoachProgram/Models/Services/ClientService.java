package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Enums.Program;

import java.util.List;

public interface ClientService {

    void addClient(ClientDTO clientDTO, String userEmail);

    List<ClientEntity> getClientForUser(UserEntity user);

    List<ClientDTO> getDTOClientsForUser(UserEntity user);

    List<ClientDTO> getClientByProgram(UserEntity user, Program program);

    List<ClientDTO> getClientsBySex(UserEntity user,String sex);

    List<ClientDTO> getClientsByActiveStatus(UserEntity user,boolean isActive);

    ClientDTO getClientById(long clientId);

    boolean giveProgress(long clientId);

    void updateCurrentWeight(long clientId, double newWeight);

    void toggleActive(long clientId);

    void update(ClientDTO clientDTO,long clientId);

    void deleteById(long clientId);

    ClientEntity getByIdOrThrow(long clientId);
}
