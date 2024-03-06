package me.croshaw.ess.settings;

import java.util.Random;

public class CarSettings extends DefaultSettings {
    private int carCount;
    private double exhaust;

    @Override
    public void fillRandomly(Random random) {
        setCarCount(random.nextInt(30, 91));
        setExhaust((double) (int) (random.nextDouble(0.5, 5) * 100) / 100);
    }

    @Override
    public void fillDefault() {
        setCarCount(30);
        setExhaust(1);
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
