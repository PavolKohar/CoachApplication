package com.palci.CoachProgram.Data.Repositories;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    List<ClientEntity> findAllByOwner(UserEntity owner);
}
