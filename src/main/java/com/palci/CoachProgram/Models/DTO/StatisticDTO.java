package com.palci.CoachProgram.Models.DTO;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Models.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;

public class StatisticDTO {
    private int allTrainings;
    private int doneTrainings;
    private double percentSuccessTraining;
    private int allPlans;
    private int donePlans;
    private double percentSuccessTrainingPlans; // TODO - check for all Plans (bad values)
    private double maxWeight;
    private double minWeight;
    private double maxDiffWeight;
    private double averageWeight;

    public StatisticDTO(int allTrainings,int doneTrainings,double percentSuccessTraining, int allPlans , int donePlans,double percentSuccessTrainingPlans,
                        double maxWeight, double minWeight , double maxDiffWeight , double averageWeight){
        this.allTrainings = allTrainings;
        this.doneTrainings = doneTrainings;
        this.percentSuccessTraining = percentSuccessTraining;
        this.allPlans = allPlans;
        this.donePlans = donePlans;
        this.percentSuccessTrainingPlans = percentSuccessTrainingPlans;
        this.maxWeight = maxWeight;
        this.minWeight = minWeight;
        this.maxDiffWeight = maxDiffWeight;
        this.averageWeight = averageWeight;
    }

    // Getters and setters


    public int getAllTrainings() {
        return allTrainings;
    }

    public void setAllTrainings(int allTrainings) {
        this.allTrainings = allTrainings;
    }

    public int getDoneTrainings() {
        return doneTrainings;
    }

    public void setDoneTrainings(int doneTrainings) {
        this.doneTrainings = doneTrainings;
    }

    public double getPercentSuccessTraining() {
        return percentSuccessTraining;
    }

    public void setPercentSuccessTraining(double percentSuccessTraining) {
        this.percentSuccessTraining = percentSuccessTraining;
    }

    public int getAllPlans() {
        return allPlans;
    }

    public void setAllPlans(int allPlans) {
        this.allPlans = allPlans;
    }

    public int getDonePlans() {
        return donePlans;
    }

    public void setDonePlans(int donePlans) {
        this.donePlans = donePlans;
    }

    public double getPercentSuccessTrainingPlans() {
        return percentSuccessTrainingPlans;
    }

    public void setPercentSuccessTrainingPlans(double percentSuccessTrainingPlans) {
        this.percentSuccessTrainingPlans = percentSuccessTrainingPlans;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public double getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(double minWeight) {
        this.minWeight = minWeight;
    }

    public double getMaxDiffWeight() {
        return maxDiffWeight;
    }

    public void setMaxDiffWeight(double maxDiffWeight) {
        this.maxDiffWeight = maxDiffWeight;
    }

    public double getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(double averageWeight) {
        this.averageWeight = averageWeight;
    }
}
