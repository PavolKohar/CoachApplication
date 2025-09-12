package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.UserRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import com.palci.CoachProgram.Models.Enums.Program;
import com.palci.CoachProgram.Models.Mappers.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ClientMapper clientMapper;
    @Autowired
    WeightRepository weightRepository;

    @Override
    public void addClient(ClientDTO clientDTO, String userEmail) {
        UserEntity owner = userRepository.findByEmail(userEmail).orElseThrow(()->new RuntimeException("User not found"));

        ClientEntity client = new ClientEntity();

        client.setOwner(owner);
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setOriginalWeight(clientDTO.getOriginalWeight());
        client.setGoalWeight(clientDTO.getGoalWeight());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setSex(clientDTO.getSex());
        client.setProgram(clientDTO.getProgram());
        client.setActive(true);

        clientRepository.save(client);
    }

    @Override
    public List<ClientEntity> getClientForUser(UserEntity user) {
        return clientRepository.findAllByOwner(user);
    }

    @Override
    public List<ClientDTO> getDTOClientsForUser(UserEntity user) {
        List<ClientEntity> clientEntities = getClientForUser(user);

        List<ClientDTO> clientDTOS = new ArrayList<>();

        for (ClientEntity client : clientEntities){
            ClientDTO dto = clientMapper.toDto(client);
            clientDTOS.add(dto);
        }

        return clientDTOS;
    }

    @Override
    public List<ClientDTO> getClientsBySex(UserEntity user, String sex) {
        return getDTOClientsForUser(user).stream().filter(c->c.getSex().equals(sex)).toList();
    }

    @Override
    public List<ClientDTO> getClientByProgram(UserEntity user, Program program) {
        return getDTOClientsForUser(user).stream().filter(c->c.getProgram().equals(program)).toList();
    }

    @Override
    public List<ClientDTO> getClientsByActiveStatus(UserEntity user, boolean isActive) {
        return getDTOClientsForUser(user).stream().filter(c->c.isActive()==isActive).toList();
    }

    @Override
    public ClientDTO getClientById(long clientId) {
        ClientEntity client = getByIdOrThrow(clientId);
        return clientMapper.toDto(client);
    }

    @Override
    public boolean giveProgress(long clientId) {
        ClientEntity client = getByIdOrThrow(clientId);
        ClientDTO clientDTO = clientMapper.toDto(client);
        return clientDTO.getProgress()>0;
    }

    @Override
    public void updateCurrentWeight(long clientId, double newWeight) {
        ClientEntity client = getByIdOrThrow(clientId);
        double oldWeight = client.getCurrentWeight();
        double weightDifference = Math.round((newWeight - oldWeight) * 100.0) / 100.0;
        client.setCurrentWeight(newWeight);

        clientRepository.save(client);

        WeightEntity entry = new WeightEntity();
        entry.setClient(client);
        entry.setNewWeight(newWeight);
        entry.setDate(LocalDate.now());
        entry.setOldWeight(oldWeight);
        entry.setDifference(weightDifference);

        weightRepository.save(entry);

    }

    @Override
    public void toggleActive(long clientId) {
        ClientEntity clientEntity = getByIdOrThrow(clientId);
        clientEntity.setActive(!clientEntity.isActive());
        clientRepository.save(clientEntity);
    }

    @Override
    public void update(ClientDTO clientDTO,long clientId) {

        ClientEntity client = getByIdOrThrow(clientId);

        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setOriginalWeight(clientDTO.getOriginalWeight());
        client.setGoalWeight(clientDTO.getGoalWeight());
        client.setSex(clientDTO.getSex());
        client.setProgram(clientDTO.getProgram());

        clientRepository.save(client);

    }
    @Override
    public void deleteById(long clientId) {
        clientRepository.deleteById(clientId);
    }

    @Override
    public ClientEntity getByIdOrThrow(long clientId){
        return clientRepository.findById(clientId).orElseThrow();
    }
}
