package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingPlanRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticServiceImpl implements StatisticService{
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    TrainingPlanRepository trainingPlanRepository;
    @Autowired
    WeightRepository weightRepository;


    @Override
    public int getAllTrainings(ClientEntity client) {
        return trainingRepository.findAllByClientOrderByDateAsc(client).size();
    }

    @Override
    public int getDoneTrainingPlans(ClientEntity client) {
        return trainingRepository.findAllByClientAndDoneTrueOrderByDateAsc(client).size();
    }

    @Override
    public double getPercentOfTrainings(ClientEntity client) {
        int allTrainings = getAllTrainings(client);
        int doneTrainings = getDoneTrainings(client);

        return (double) (doneTrainings / allTrainings) * 100;
    }

    @Override
    public int getAllTrainingPlans(ClientEntity client) {
        return trainingPlanRepository.findAllByClientOrderByStartDateAsc(client).size();
    }

    @Override
    public int getDoneTrainings(ClientEntity client) {
        return  trainingPlanRepository.findAllByClientAndDoneTrue(client).size();
    }

    @Override
    public double getPercentOfTrainingPlans(ClientEntity client) {
        int allPlans = getAllTrainingPlans(client);
        int donePlans = getDoneTrainingPlans(client);

        return (double) (donePlans / allPlans) * 100;
    }

    @Override
    public double getMaxWeight(ClientEntity client) {
        return weightRepository.findTopByClientOrderByOldWeightDesc(client)
                .map(WeightEntity::getOldWeight)
                .orElse(client.getOriginalWeight());
    }

    @Override
    public double getMinWeight(ClientEntity client) {
        return weightRepository.findTopByClientOrderByNewWeightAsc(client)
                .map(WeightEntity::getNewWeight)
                .orElse(client.getOriginalWeight());
    }

    @Override
    public double getMaxWeightDifference(ClientEntity client) {
        return getMaxWeight(client)  - getMinWeight(client);
    }
}
