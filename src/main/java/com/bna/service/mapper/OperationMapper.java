package com.bna.service.mapper;

import com.bna.domain.Compte;
import com.bna.domain.Operation;
import com.bna.service.dto.CompteDTO;
import com.bna.service.dto.OperationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Operation} and its DTO {@link OperationDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperationMapper extends EntityMapper<OperationDTO, Operation> {
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteId")
    OperationDTO toDto(Operation s);

    @Named("compteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteDTO toDtoCompteId(Compte compte);
}
