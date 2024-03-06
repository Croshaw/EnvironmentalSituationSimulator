package me.croshaw.ess.settings;

import java.io.Serializable;
import java.time.Duration;
import java.util.Random;

public class FilterSettings extends DefaultSettings implements Serializable {
    private double price;
    private Duration installationDuration;
    private float emissionReduction;
    @Override
    public void fillRandomly(Random random) {
        setPrice((double) (int) (random.nextDouble(20000, 50000) * 100) / 100);
        setInstallationDuration(Duration.ofDays(random.nextInt(7, 11)));
        setEmissionReduction((float) (int) (random.nextFloat(0.01f, 0.3f) * 100) / 100);
    }

    @Override
    public void fillDefault() {
        setPrice(30000);
        setInstallationDuration(Duration.ofDays(7));
        setEmissionReduction(.07f);
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
