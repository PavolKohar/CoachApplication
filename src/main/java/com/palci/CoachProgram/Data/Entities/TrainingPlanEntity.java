package com.palci.CoachProgram.Data.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TrainingPlans")
public class TrainingPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long trainingPlanId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int totalWorkouts;

    @Column(nullable = false)
    private int doneWorkouts;

    @Column(nullable = false)
    private boolean done;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id")
    private UserEntity user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @OneToMany(mappedBy = "plan",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingEntity> trainings;

    public void updateProgress(){
        if (trainings!= null){
            this.doneWorkouts  = (int) trainings.stream().filter(TrainingEntity::isDone).count();
            this.done = (doneWorkouts == totalWorkouts);
        }
    }



    // Getters and setters



    public long getTrainingPlanId() {
        return trainingPlanId;
    }

    public void setTrainingPlanId(long trainingPlanId) {
        this.trainingPlanId = trainingPlanId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public List<TrainingEntity> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingEntity> trainings) {
        this.trainings = trainings;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getTotalWorkouts() {
        return totalWorkouts;
    }

    public void setTotalWorkouts(int totalWorkouts) {
        this.totalWorkouts = totalWorkouts;
    }

    public int getDoneWorkouts() {
        return doneWorkouts;
    }

    public void setDoneWorkouts(int doneWorkouts) {
        this.doneWorkouts = doneWorkouts;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
