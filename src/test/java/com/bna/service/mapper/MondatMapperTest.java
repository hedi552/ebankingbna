package com.bna.service.mapper;

import static com.bna.domain.MondatAsserts.*;
import static com.bna.domain.MondatTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MondatMapperTest {

    private MondatMapper mondatMapper;

    @BeforeEach
    void setUp() {
        mondatMapper = new MondatMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMondatSample1();
        var actual = mondatMapper.toEntity(mondatMapper.toDto(expected));
        assertMondatAllPropertiesEquals(expected, actual);
    }
}
