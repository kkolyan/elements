package net.kkolyan.elements.engine.utils;

/**
 * @author nplekhanov
 */
public class RateCounter {
    private double rate;
    private long interval;
    private int ticks;
    private long intervalStartedAt;

    public RateCounter(long interval) {
        this.interval = interval;
    }

    public void tick() {
        ticks ++;
    }

    public double getRate() {
        if (System.currentTimeMillis() - intervalStartedAt > interval) {
            rate = 1000.0 * ticks / (System.currentTimeMillis() - intervalStartedAt);
            intervalStartedAt = System.currentTimeMillis();
            ticks = 0;
        }
        return rate;
    }
}
