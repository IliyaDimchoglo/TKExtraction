package com.tkextraction.utils;

import java.util.concurrent.ThreadLocalRandom;

public class TransactionRandomizer {

    private static final Long NUMBER_FROM = 11111111111111111L;
    private static final Long NUMBER_TO = 99999999999999999L;

    public static Long getRandomIdTransaction() {
        return ThreadLocalRandom.current().longs(NUMBER_FROM, NUMBER_TO)
                .findFirst()
                .getAsLong();
    }
}
