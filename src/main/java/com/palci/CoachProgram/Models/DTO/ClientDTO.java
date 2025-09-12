package com.palci.CoachProgram.Models.DTO;

import com.palci.CoachProgram.Models.Enums.Program;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.Period;

public class ClientDTO {

    private long clientId;


    @NotBlank(message = "Please fill in the name")
    private String firstName;

    @NotBlank(message = "Please fill in the last name")
    private String lastName;

    @NotNull(message = "Please fill in the weight")
    @Positive(message = "Weight must be greater than 0")
    private double originalWeight;

    private double currentWeight = originalWeight;

    @NotNull(message = "Please fill in the goal weight")
    @Positive(message = "Goal weight must be greater than 0")
    private double goalWeight;

    @NotNull(message = "Please fill in the birth date")
    @Past(message = "The birth date can't be in future")
    private LocalDate birthDate;

    @NotNull(message = "Please choose some option")
    private String sex;

    @NotNull(message = "Please fill in the program")
    private Program program;

    private boolean active = true;

    private long ownerId;



    public int getProgress() {
        double totalChange = Math.abs(goalWeight - originalWeight);
        double achievedChange = Math.abs(currentWeight - originalWeight);

        if (totalChange == 0) {
            return 0;
        }

        if ((goalWeight>originalWeight)&&(currentWeight<originalWeight)){
            return 0;
        } else if ((goalWeight<originalWeight)&&(currentWeight>originalWeight)) {
            return 0;
        }

        double progress = (achievedChange / totalChange) * 100;
        return (int) Math.round(progress);
    }


    // Getters and setters




    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getAge() {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getOriginalWeight() {
        return originalWeight;
    }

    public void setOriginalWeight(double originalWeight) {
        this.originalWeight = originalWeight;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
