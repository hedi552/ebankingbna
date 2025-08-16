package com.bna.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VirementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Virement getVirementSample1() {
        return new Virement().id(1L).compteBeneficiaire("compteBeneficiaire1").motif("motif1");
    }

    public static Virement getVirementSample2() {
        return new Virement().id(2L).compteBeneficiaire("compteBeneficiaire2").motif("motif2");
    }

    public static Virement getVirementRandomSampleGenerator() {
        return new Virement()
            .id(longCount.incrementAndGet())
            .compteBeneficiaire(UUID.randomUUID().toString())
            .motif(UUID.randomUUID().toString());
    }
}
