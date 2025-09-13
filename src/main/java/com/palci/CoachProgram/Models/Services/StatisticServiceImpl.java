package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import com.palci.CoachProgram.Data.Repositories.ClientRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingPlanRepository;
import com.palci.CoachProgram.Data.Repositories.TrainingRepository;
import com.palci.CoachProgram.Data.Repositories.WeightRepository;
import com.palci.CoachProgram.Models.DTO.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
        return trainingRepository.countByClientAndDateLessThanEqual(client,LocalDate.now());
    }

    @Override
    public int getDoneTrainingPlans(ClientEntity client) {
        return trainingPlanRepository.findAllByClientAndDoneTrue(client).size();
    }

    @Override
    public double getPercentOfTrainings(ClientEntity client) {
        int allTrainings = getAllTrainings(client);
        int doneTrainings = getDoneTrainings(client);

        if (allTrainings == 0) return 0.0;

        return  ((double)doneTrainings / allTrainings) * 100;
    }

    @Override
    public int getAllTrainingPlans(ClientEntity client) {
        return trainingPlanRepository.findAllByClientOrderByStartDateAsc(client).size();
    }

    @Override
    public int getDoneTrainings(ClientEntity client) {
        return  trainingRepository.findAllByClientAndDoneTrueOrderByDateAsc(client).size();
    }

    @Override
    public double getPercentOfTrainingPlans(ClientEntity client) {
        int allPlans = getAllTrainingPlans(client);
        int donePlans = getDoneTrainingPlans(client);

        if (allPlans == 0) return 0.0;

        return ((double) donePlans / allPlans) * 100;
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

    @Override
    public double getAverageWeight(ClientEntity client) {
        List<WeightEntity> allWeights = weightRepository.findAllByClient(client);
        return allWeights.stream()
                .mapToDouble(WeightEntity::getNewWeight)
                .average()
                .orElse(client.getOriginalWeight());


    }

    @Override
    public StatisticDTO getStatistic(ClientEntity client) {
        int allTrainings = getAllTrainings(client);
        int doneTrainings = getDoneTrainings(client);
        double successRateTrainings = getPercentOfTrainings(client);
        successRateTrainings = (double) Math.round(successRateTrainings * 100) / 100;
        int allTrainingsPlans = getAllTrainingPlans(client);
        int doneTrainingsPlans = getDoneTrainingPlans(client);
        double trainingPlansSuccess = getPercentOfTrainingPlans(client);
        trainingPlansSuccess = (double) Math.round(trainingPlansSuccess * 100) / 100;
        double maxWeight = getMaxWeight(client);
        double minWeight = getMinWeight(client);
        double maxWeightDiff = getMaxWeightDifference(client);
        maxWeightDiff = (double) Math.round(maxWeightDiff*100)/100;
        double averageWeight = getAverageWeight(client);
        averageWeight = (double) Math.round(averageWeight * 100) /100;



        return new StatisticDTO(allTrainings,doneTrainings,successRateTrainings,allTrainingsPlans,doneTrainingsPlans,trainingPlansSuccess,maxWeight,minWeight,maxWeightDiff,averageWeight);
    }
}
