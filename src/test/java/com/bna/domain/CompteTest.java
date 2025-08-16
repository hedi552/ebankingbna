package com.bna.domain;

import static com.bna.domain.CompteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compte.class);
        Compte compte1 = getCompteSample1();
        Compte compte2 = new Compte();
        assertThat(compte1).isNotEqualTo(compte2);

        compte2.setId(compte1.getId());
        assertThat(compte1).isEqualTo(compte2);

        compte2 = getCompteSample2();
        assertThat(compte1).isNotEqualTo(compte2);
    }
}
