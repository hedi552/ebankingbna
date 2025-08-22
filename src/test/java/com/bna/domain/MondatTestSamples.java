package com.bna.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MondatTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Mondat getMondatSample1() {
        return new Mondat().id(1L).compteSrc("compteSrc1").compteBenef("compteBenef1").code("code1");
    }

    public static Mondat getMondatSample2() {
        return new Mondat().id(2L).compteSrc("compteSrc2").compteBenef("compteBenef2").code("code2");
    }

    public static Mondat getMondatRandomSampleGenerator() {
        return new Mondat()
            .id(longCount.incrementAndGet())
            .compteSrc(UUID.randomUUID().toString())
            .compteBenef(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
