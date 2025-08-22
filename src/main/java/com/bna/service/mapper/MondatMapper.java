package com.bna.service.mapper;

import com.bna.domain.Compte;
import com.bna.domain.Mondat;
import com.bna.service.dto.CompteDTO;
import com.bna.service.dto.MondatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mondat} and its DTO {@link MondatDTO}.
 */
@Mapper(componentModel = "spring")
public interface MondatMapper extends EntityMapper<MondatDTO, Mondat> {
    @Mapping(target = "compte", source = "compte", qualifiedByName = "compteId")
    MondatDTO toDto(Mondat s);

    @Named("compteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompteDTO toDtoCompteId(Compte compte);
}
