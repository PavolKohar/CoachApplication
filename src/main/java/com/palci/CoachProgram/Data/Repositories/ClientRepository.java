package com.palci.CoachProgram.Data.Repositories;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Data.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
    List<ClientEntity> findAllByOwner(UserEntity owner);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.clients WHERE u.userId = :id")
    Optional<UserEntity> findByIdWithClients(@Param("id") Long id);
}
