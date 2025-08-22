package com.bna.domain;

import static com.bna.domain.CompteTestSamples.*;
import static com.bna.domain.MondatTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MondatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mondat.class);
        Mondat mondat1 = getMondatSample1();
        Mondat mondat2 = new Mondat();
        assertThat(mondat1).isNotEqualTo(mondat2);

        mondat2.setId(mondat1.getId());
        assertThat(mondat1).isEqualTo(mondat2);

        mondat2 = getMondatSample2();
        assertThat(mondat1).isNotEqualTo(mondat2);
    }

    @Test
    void compteTest() {
        Mondat mondat = getMondatRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        mondat.setCompte(compteBack);
        assertThat(mondat.getCompte()).isEqualTo(compteBack);

        mondat.compte(null);
        assertThat(mondat.getCompte()).isNull();
    }
}
