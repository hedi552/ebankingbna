package com.bna.domain;

import static com.bna.domain.CompteTestSamples.*;
import static com.bna.domain.OperationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bna.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OperationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operation.class);
        Operation operation1 = getOperationSample1();
        Operation operation2 = new Operation();
        assertThat(operation1).isNotEqualTo(operation2);

        operation2.setId(operation1.getId());
        assertThat(operation1).isEqualTo(operation2);

        operation2 = getOperationSample2();
        assertThat(operation1).isNotEqualTo(operation2);
    }

    @Test
    void compteTest() {
        Operation operation = getOperationRandomSampleGenerator();
        Compte compteBack = getCompteRandomSampleGenerator();

        operation.setCompte(compteBack);
        assertThat(operation.getCompte()).isEqualTo(compteBack);

        operation.compte(null);
        assertThat(operation.getCompte()).isNull();
    }
}
