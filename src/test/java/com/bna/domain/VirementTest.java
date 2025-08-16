package com.bna.domain;

import static com.bna.domain.CompteTestSamples.*;
import static com.bna.domain.VirementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VirementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Virement.class);
        Virement virement1 = getVirementSample1();
        Virement virement2 = new Virement();
        assertThat(virement1).isNotEqualTo(virement2);

        virement2.setId(virement1.getId());
        assertThat(virement1).isEqualTo(virement2);

        virement2 = getVirementSample2();
        assertThat(virement1).isNotEqualTo(virement2);
    }

    @Test
    void compteTest() {
        Virement virement = getVirementRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        virement.setCompte(compteBack);
        assertThat(virement.getCompte()).isEqualTo(compteBack);

        virement.compte(null);
        assertThat(virement.getCompte()).isNull();
    }

    @Test
    void beneficiaireTest() {
        Virement virement = getVirementRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        virement.setBeneficiaire(compteBack);
        assertThat(virement.getBeneficiaire()).isEqualTo(compteBack);

        virement.beneficiaire(null);
        assertThat(virement.getBeneficiaire()).isNull();
    }
}
