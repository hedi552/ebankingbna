package com.bna.service.mapper;

import static com.bna.domain.VirementAsserts.*;
import static com.bna.domain.VirementTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VirementMapperTest {

    private VirementMapper virementMapper;

    @BeforeEach
    void setUp() {
        virementMapper = new VirementMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVirementSample1();
        var actual = virementMapper.toEntity(virementMapper.toDto(expected));
        assertVirementAllPropertiesEquals(expected, actual);
    }
}
