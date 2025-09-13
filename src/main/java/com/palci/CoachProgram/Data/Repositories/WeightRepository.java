package com.palci.CoachProgram.Data.Repositories;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeightRepository extends JpaRepository<WeightEntity,Long> {
    List<WeightEntity> findAllByClientOrderByDateAsc(ClientEntity client);
    List<WeightEntity> findTop5ByClientOrderByDateDesc(ClientEntity client);
    Optional<WeightEntity> findTopByClientOrderByOldWeightDesc(ClientEntity client);
    Optional<WeightEntity> findTopByClientOrderByNewWeightAsc(ClientEntity client);

}
