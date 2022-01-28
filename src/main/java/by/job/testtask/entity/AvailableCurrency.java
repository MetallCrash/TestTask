package by.job.testtask.entity;

import java.util.stream.Stream;

public enum AvailableCurrency {
    BTC_ID(90),
    ETH_ID(80),
    SOL_ID(48543);

    private final long id;

    AvailableCurrency(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static Stream<AvailableCurrency> stream() {
        return Stream.of(AvailableCurrency.values());
    }
}
