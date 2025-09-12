package com.palci.CoachProgram.Data.Repositories;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.WeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightRepository extends JpaRepository<WeightEntity,Long> {
    List<WeightEntity> findAllByClientOrderByDateAsc(ClientEntity client);

}
