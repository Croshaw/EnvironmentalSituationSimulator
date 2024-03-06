package me.croshaw.ess.model;

public class Weather {
    private String name;
    private float multiplier;

    public Weather(String name, float multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public float getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        return name;
    }
}
