package com.palci.CoachProgram.Data.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "weightHistory")
public class WeightEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long changeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @Column(nullable = false)
    private double oldWeight;

    @Column(nullable = false)
    private double newWeight;

    @Column(nullable = false)
    private double difference;

    @Column(nullable = false)
    private LocalDate date;

    // Getters and setters


    public long getChangeId() {
        return changeId;
    }

    public void setChangeId(long changeId) {
        this.changeId = changeId;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public double getOldWeight() {
        return oldWeight;
    }

    public void setOldWeight(double oldWeight) {
        this.oldWeight = oldWeight;
    }

    public double getNewWeight() {
        return newWeight;
    }

    public void setNewWeight(double newWeight) {
        this.newWeight = newWeight;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
