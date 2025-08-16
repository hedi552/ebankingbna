package com.bna.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CompteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Compte getCompteSample1() {
        return new Compte().id(1L).numcompte("numcompte1").agence(1);
    }

    public static Compte getCompteSample2() {
        return new Compte().id(2L).numcompte("numcompte2").agence(2);
    }

    public static Compte getCompteRandomSampleGenerator() {
        return new Compte().id(longCount.incrementAndGet()).numcompte(UUID.randomUUID().toString()).agence(intCount.incrementAndGet());
    }
}
