package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;

public interface StatisticService {

    int getAllTrainings(ClientEntity client);

    int getDoneTrainings(ClientEntity client);

    double getPercentOfTrainings(ClientEntity client);

    int getAllTrainingPlans(ClientEntity client);

    int getDoneTrainingPlans(ClientEntity client);

    double getPercentOfTrainingPlans(ClientEntity client);

    double getMinWeight(ClientEntity client);

    double getMaxWeight(ClientEntity client);

    double getMaxWeightDifference(ClientEntity client);


}
