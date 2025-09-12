package com.palci.CoachProgram.Data.Repositories;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.TrainingPlanEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlanEntity, Long> {
    List<TrainingPlanEntity> findAllByClientOrderByStartDateAsc(ClientEntity client);
    List<TrainingPlanEntity> findAllByUserOrderByStartDateAsc(UserEntity user);
}
