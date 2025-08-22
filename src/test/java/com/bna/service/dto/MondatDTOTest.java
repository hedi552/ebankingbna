package com.bna.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MondatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MondatDTO.class);
        MondatDTO mondatDTO1 = new MondatDTO();
        mondatDTO1.setId(1L);
        MondatDTO mondatDTO2 = new MondatDTO();
        assertThat(mondatDTO1).isNotEqualTo(mondatDTO2);
        mondatDTO2.setId(mondatDTO1.getId());
        assertThat(mondatDTO1).isEqualTo(mondatDTO2);
        mondatDTO2.setId(2L);
        assertThat(mondatDTO1).isNotEqualTo(mondatDTO2);
        mondatDTO1.setId(null);
        assertThat(mondatDTO1).isNotEqualTo(mondatDTO2);
    }
}
