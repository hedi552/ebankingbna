package com.bna.service.mapper;

import com.bna.domain.Compte;
import com.bna.domain.Virement;
import com.bna.service.dto.CompteDTO;
import com.bna.service.dto.VirementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Virement} and its DTO {@link VirementDTO}.
 */
@Mapper(componentModel = "spring")
public interface VirementMapper extends EntityMapper<VirementDTO, Virement> {
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteId")
    @Mapping(target = "beneficiaire", source = "beneficiaire", qualifiedByName = "compteId")
    VirementDTO toDto(Virement s);

    @Named("compteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteDTO toDtoCompteId(Compte compte);
}
