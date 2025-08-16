package com.bna.service.mapper;

import com.bna.domain.Compte;
import com.bna.domain.User;
import com.bna.service.dto.CompteDTO;
import com.bna.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compte} and its DTO {@link CompteDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteMapper extends EntityMapper<CompteDTO, Compte> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    CompteDTO toDto(Compte s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
