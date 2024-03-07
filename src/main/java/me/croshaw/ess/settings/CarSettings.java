package me.croshaw.ess.settings;

import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Range;

import java.io.Serializable;
import java.util.Random;

public class CarSettings extends DefaultSettings implements Serializable {
    public static final Range<Integer> CAR_COUNT_RANGE = new Range<>(30, 90);
    public static final Range<Double> AVG_EXHAUST_RANGE = new Range<>(0.5d, 5d);
    private int carCount;
    private double exhaust;

    @Override
    public void fillRandomly(Random random) {
        setCarCount(CAR_COUNT_RANGE.getRandom(random));
        setExhaust(NumberHelper.round(AVG_EXHAUST_RANGE.getRandom(random), 2));
    }

    @Override
    public void fillDefault() {
        setCarCount(CAR_COUNT_RANGE.getMin());
        setExhaust(AVG_EXHAUST_RANGE.getMin());
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    public double getExhaust() {
        return exhaust;
    }

    public void setExhaust(double exhaust) {
        this.exhaust = exhaust;
    }
}
