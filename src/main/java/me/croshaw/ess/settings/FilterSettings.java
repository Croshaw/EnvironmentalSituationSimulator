package me.croshaw.ess.settings;

import me.croshaw.ess.util.NumberHelper;
import me.croshaw.ess.util.Range;

import java.io.Serializable;
import java.time.Duration;
import java.util.Random;

public class FilterSettings extends DefaultSettings implements Serializable {
    public static final Range<Double> PRICE_RANGE = new Range<>(20000d, 50000d);
    public static final Range<Integer> INSTALLATION_DURATION_IN_DAYS_RANGE = new Range<>(7, 11);
    public static final Range<Float> EMISSION_REDUCTION_RANGE = new Range<>(0.01f, 0.3f);
    private double price;
    private Duration installationDuration;
    private float emissionReduction;
    @Override
    public void fillRandomly(Random random) {
        setPrice(NumberHelper.round(PRICE_RANGE.getRandom(random), 2));
        setInstallationDuration(Duration.ofDays(INSTALLATION_DURATION_IN_DAYS_RANGE.getRandom(random)));
        setEmissionReduction(NumberHelper.round(EMISSION_REDUCTION_RANGE.getRandom(random), 2));
    }

    @Override
    public void fillDefault() {
        setPrice(PRICE_RANGE.getMin());
        setInstallationDuration(Duration.ofDays(INSTALLATION_DURATION_IN_DAYS_RANGE.getMin()));
        setEmissionReduction(EMISSION_REDUCTION_RANGE.getMin());
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Duration getInstallationDuration() {
        return installationDuration;
    }

    public void setInstallationDuration(Duration installationDuration) {
        this.installationDuration = installationDuration;
    }

    public float getEmissionReduction() {
        return emissionReduction;
    }

    public void setEmissionReduction(float emissionReduction) {
        this.emissionReduction = emissionReduction;
    }
}
