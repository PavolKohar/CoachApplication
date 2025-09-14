package com.palci.CoachProgram.Models.Services;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import com.palci.CoachProgram.Models.DTO.TrainingDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TrainingService {

    List<TrainingEntity> getTodayTrainings(UserEntity user);

    List<String> getTodayTrainingsString(UserEntity user);

    void createPlan(ClientEntity client, UserEntity owner, int weeks, int trainingsPerWeek, LocalDate startDate, LocalTime time);

    void updateTraining(long trainingId, TrainingDTO trainingDTO);

    TrainingDTO toDto(TrainingEntity entity);

    void createTraining(long clientId,TrainingDTO trainingDTO);

    List<TrainingEntity> getThisWeekTrainings(UserEntity user);

    List<TrainingEntity> getNextWeekTrainings(UserEntity user);

    List<TrainingEntity> getThisMonthTrainings(UserEntity user);
}
