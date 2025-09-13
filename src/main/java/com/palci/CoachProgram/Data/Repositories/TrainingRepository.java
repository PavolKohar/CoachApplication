package com.palci.CoachProgram.Data.Repositories;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.TrainingEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<TrainingEntity,Long> {
    List<TrainingEntity> findAllByClientOrderByDateAsc(ClientEntity client);
    List<TrainingEntity> findAllByUserOrderByDateAsc(UserEntity user);
    List<TrainingEntity> findAllByClientAndDoneTrueOrderByDateAsc(ClientEntity client);
    List<TrainingEntity> findTop5ByClientOrderByDateAsc(ClientEntity client);
    List<TrainingEntity> findTop5ByClientAndDoneFalseOrderByDateAsc(ClientEntity client);
}
