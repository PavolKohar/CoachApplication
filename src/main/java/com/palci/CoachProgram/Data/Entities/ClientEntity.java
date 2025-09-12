package com.palci.CoachProgram.Data.Entities;

import com.palci.CoachProgram.Models.Enums.Program;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clientId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private double originalWeight;

    @Column(nullable = false)
    private double currentWeight;

    @Column(nullable = false)
    private double goalWeight;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String sex;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Program program;

    @Column(nullable = false)
    private boolean active;

    @PrePersist
    void prePersist() {
        if (this.currentWeight == 0) {
            this.currentWeight = this.originalWeight;
        }
    }

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL,orphanRemoval = true)
    List<WeightEntity> weightHistory = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingEntity> trainings = new ArrayList<>();
// ------------------- Getters and Setters ------

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
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

    public List<WeightEntity> getWeightHistory() {
        return weightHistory;
    }

    public void setWeightHistory(List<WeightEntity> weightHistory) {
        this.weightHistory = weightHistory;
    }

    public List<TrainingEntity> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingEntity> trainings) {
        this.trainings = trainings;
    }

    public String getFullName(){
        return firstName + " " + lastName ;
    }
}
