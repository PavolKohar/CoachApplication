package com.palci.CoachProgram.Models.Mappers;

import com.palci.CoachProgram.Data.Entities.ClientEntity;
import com.palci.CoachProgram.Models.DTO.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "owner.userId", target = "ownerId")
    ClientDTO toDto(ClientEntity source);

    @Mapping(source = "ownerId", target = "owner.userId")
    ClientEntity toEntity(ClientDTO source);

    void updateClientDTO(ClientDTO source, @MappingTarget ClientDTO target);

    void updateClientEntity(ClientDTO source, @MappingTarget ClientEntity target);
}
